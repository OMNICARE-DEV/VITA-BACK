package com.hops.hops_new_api.common.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hops.hops_new_api.common.exception.HopsCode;
import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.mapper.NiceCertificationMapper;
import com.hops.hops_new_api.common.model.Request.*;
import com.hops.hops_new_api.common.model.Response.GetNiceEncryptTokenResponse;
import com.hops.hops_new_api.common.model.Response.NiceCertificateAuthResponse;
import com.hops.hops_new_api.common.model.Response.NiceCirtificationResponse;
import com.hops.hops_new_api.common.model.Response.RegKeyResponse;
import com.hops.hops_new_api.common.model.data.*;
import com.hops.hops_new_api.common.model.util.AES256Util;
import com.hops.hops_new_api.common.model.util.ValidUtil;
import com.hops.hops_new_api.common.service.NiceCertificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Service
public class NiceCertificationServiceImpl implements NiceCertificationService {
    private static final Logger logger = LoggerFactory.getLogger(NiceCertificationServiceImpl.class);

    private final NiceCertificationMapper mapper;

    public NiceCertificationServiceImpl(NiceCertificationMapper mapper) {
        this.mapper = mapper;
    }

    @Value("${nice.encryptToken}")
    private String encryptTokenUrl;

    String ALG = "AES/CBC/PKCS5Padding";

    final String KEY_ID_PHONE = "2001";
    final String IV_ID_PHONE = "2002";
    final String HMAC_ID_PHONE = "2003";
    final String SITE_CODE_ID_PHONE = "2004";
    final String TOKEN_VERSION_ID_PHONE = "2005";
    final String PRODUCT_ID_PHONE = "2006";

    final String KEY_ID_IPIN = "2011";
    final String IV_ID_IPIN = "2012";
    final String HMAC_ID_IPIN = "2013";
    final String SITE_CODE_ID_IPIN = "2014";
    final String TOKEN_VERSION_ID_IPIN = "2015";
    final String PRODUCT_ID_IPIN = "2007";

    final String ACCESS_TOKEN = "2010";
    final String CLIENT_ID = "2008";

    @Override
    public NiceCirtificationResponse userJoinCertification(NiceCertificationRequest request) throws HopsException {

        NiceCirtificationResponse niceCirtificationResponse = new NiceCirtificationResponse();

        /* 필수값 체크*/
        ValidUtil.validNull(
                request.getReturnUrl(),
                request.getUserCertifyDiv(),
                request.getUserCertifyType()
        );

        //1.인증 db입력
        UserCertifyDto userCertifyDto = new UserCertifyDto();
        userCertifyDto.setUserCertifyDiv(request.getUserCertifyDiv());
        userCertifyDto.setUserCertifyType(request.getUserCertifyType());
        userCertifyDto.setCertifySt("10"); // 인증상태 10:인증등록 50:인증확인 70:인증완료

        try {
            int regCount = mapper.regUserCertify(userCertifyDto);

            if(regCount == 0){
                throw new HopsException(HopsCode.DATABASE_ERROR);
            }else{
                ValidUtil.validNull(userCertifyDto.getUserCertifyNo());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new HopsException(HopsCode.DATABASE_ERROR);
        }

        //2.기존의 대칭키 생성 정보 조회 : 50분 limit
        String keyValue = "";
        String ivValue = "";
        String hmacValue = "";
        String siteCodeValue = "";
        String tokenVersionIdValue = "";
        String productId = "";
        String accessToken = "";
        String clientId = "";

        try {
            accessToken = mapper.getServiceStaticInfo(ACCESS_TOKEN);
            clientId = mapper.getServiceStaticInfo(CLIENT_ID);
        }catch (Exception e){
            mapper.updtStTo80(userCertifyDto.getUserCertifyNo());
            e.printStackTrace();
            throw new HopsException(HopsCode.DATABASE_ERROR);
        }

        if (request.getUserCertifyType().equals("10")) { // 핸드폰 본인인증이라면
            try {
                keyValue = mapper.getServiceInfo(KEY_ID_PHONE);
                ivValue = mapper.getServiceInfo(IV_ID_PHONE);
                hmacValue = mapper.getServiceInfo(HMAC_ID_PHONE);
                siteCodeValue = mapper.getServiceInfo(SITE_CODE_ID_PHONE);
                tokenVersionIdValue = mapper.getServiceInfo(TOKEN_VERSION_ID_PHONE);
                productId = mapper.getServiceStaticInfo(PRODUCT_ID_PHONE);
            } catch (Exception e) {
                mapper.updtStTo80(userCertifyDto.getUserCertifyNo());
                e.printStackTrace();
                throw new HopsException(HopsCode.DATABASE_ERROR);
            }
        } else if(request.getUserCertifyType().equals("30")) { // 아이핀 본인인증이라면
            try {
                keyValue = mapper.getServiceInfo(KEY_ID_IPIN);
                ivValue = mapper.getServiceInfo(IV_ID_IPIN);
                hmacValue = mapper.getServiceInfo(HMAC_ID_IPIN);
                siteCodeValue = mapper.getServiceInfo(SITE_CODE_ID_IPIN);
                tokenVersionIdValue = mapper.getServiceInfo(TOKEN_VERSION_ID_IPIN);
                productId = mapper.getServiceStaticInfo(PRODUCT_ID_IPIN);
            } catch (Exception e) {
                mapper.updtStTo80(userCertifyDto.getUserCertifyNo());
                e.printStackTrace();
                throw new HopsException(HopsCode.DATABASE_ERROR);
            }
        }

        // 대칭키 생성에 필요한 값들의 발급시간이 50분 초과되었을 경우
        if (ValidUtil.isEmpty(keyValue) || ValidUtil.isEmpty(ivValue)
                || ValidUtil.isEmpty(hmacValue) || ValidUtil.isEmpty(hmacValue)
                || ValidUtil.isEmpty(siteCodeValue) || ValidUtil.isEmpty(tokenVersionIdValue)
                || ValidUtil.isEmpty(productId)){

            //3. 대칭키 요청
            RegKeyRequest regKeyRequest = new RegKeyRequest();

            regKeyRequest.setRequestId(String.valueOf(userCertifyDto.getUserCertifyNo()));
            regKeyRequest.setReqDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

            switch (request.getUserCertifyType()) {
                case "10": // 휴대폰 본인인인증의 경우
                    regKeyRequest.setCertifyType("10"); // 휴대폰
                    break;
                case "30": // 아이핀 인증의 경우
                    regKeyRequest.setCertifyType("20"); // 아이핀
                    break;
                default:
                    logger.warn("유효하지 않은 인증구분값, {}", request.getUserCertifyType());
                    throw new HopsException(HopsCode.INVALID_PARAMETER);
            }

            regKeyRequest.setProductId(productId);
            regKeyRequest.setAccessToken(accessToken);
            regKeyRequest.setClientId(clientId);

            RegKeyResponse regKeyResponse = selfAuthRegKey(regKeyRequest);

            if(ValidUtil.isEmpty(regKeyResponse)){
                throw new HopsException(HopsCode.INVALID_PARAMETER);
            }

            ServiceInfoDto serviceInfoDto = new ServiceInfoDto();
            if (request.getUserCertifyType().equals("10")) {
                serviceInfoDto.setKeyId(KEY_ID_PHONE);
                serviceInfoDto.setIvId(IV_ID_PHONE);
                serviceInfoDto.setHmacId(HMAC_ID_PHONE);
                serviceInfoDto.setSiteCodeId(SITE_CODE_ID_PHONE);
                serviceInfoDto.setTokenVersionId(TOKEN_VERSION_ID_PHONE);
                try {
                    mapper.updtServiceInfoRegDt(serviceInfoDto);
                } catch (Exception e) {
                    mapper.updtStTo80(userCertifyDto.getUserCertifyNo());
                    logger.warn("DB error (UpdtServiceInfoRegDt)", e);
                    throw new HopsException(HopsCode.DATABASE_ERROR); // DB error
                }

                // 발급 정보
                try {
                    mapper.updtIvPhone(regKeyResponse.getIv());
                    mapper.updtKeyPhone(regKeyResponse.getKey());
                    mapper.updtHmacKeyPhone(regKeyResponse.getHmacKey());
                    mapper.updtSiteCodePhone(regKeyResponse.getSiteCode());
                    mapper.updtTokenVersionIdPhone(regKeyResponse.getTokenVersionId());
                } catch (Exception e) {
                    mapper.updtStTo80(userCertifyDto.getUserCertifyNo());
                    logger.warn("DB error", e);
                    throw new HopsException(HopsCode.DATABASE_ERROR); // DB error
                }
            } else if (request.getUserCertifyType().equals("30")) { // ipin 인증이라면
                serviceInfoDto.setKeyId(KEY_ID_IPIN);
                serviceInfoDto.setIvId(IV_ID_IPIN);
                serviceInfoDto.setHmacId(HMAC_ID_IPIN);
                serviceInfoDto.setSiteCodeId(SITE_CODE_ID_IPIN);
                serviceInfoDto.setTokenVersionId(TOKEN_VERSION_ID_IPIN);
                try {
                    mapper.updtServiceInfoRegDt(serviceInfoDto);
                } catch (Exception e) {
                    mapper.updtStTo80(userCertifyDto.getUserCertifyNo());
                    logger.warn("DB error (UpdtServiceInfoRegDt)", e);
                    throw new HopsException(HopsCode.DATABASE_ERROR); // DB error
                }

                // 발급 정보
                try {
                    mapper.updtIvIpin(regKeyResponse.getIv());
                    mapper.updtKeyIpin(regKeyResponse.getKey());
                    mapper.updtHmacKeyIpin(regKeyResponse.getHmacKey());
                    mapper.updtSiteCodeIpin(regKeyResponse.getSiteCode());
                    mapper.updtTokenVersionIdIpin(regKeyResponse.getTokenVersionId());
                } catch (Exception e) {
                    mapper.updtStTo80(userCertifyDto.getUserCertifyNo());
                    logger.warn("DB error", e);
                    throw new HopsException(HopsCode.DATABASE_ERROR); // DB error
                }
            }

            // key, iv 설정
            keyValue = regKeyResponse.getKey();
            ivValue = regKeyResponse.getIv();
            hmacValue = regKeyResponse.getHmacKey();
            siteCodeValue = regKeyResponse.getSiteCode();
            tokenVersionIdValue = regKeyResponse.getTokenVersionId();

        }

        /* 4. 요청 json 암호화 */
        NicePopupDto nicePopupDto = new NicePopupDto();
        nicePopupDto.setRequestno(String.valueOf(userCertifyDto.getUserCertifyNo()));
        nicePopupDto.setReturnurl(request.getReturnUrl());
        nicePopupDto.setSitecode(siteCodeValue);

        if (request.getUserCertifyType().equals("10")) { // 휴대폰 인증일 경우
            nicePopupDto.setPopupyn("Y");
            nicePopupDto.setReceivedata("");
            // 미사용
            nicePopupDto.setAuthtype("");
            nicePopupDto.setMobilceco("");
            nicePopupDto.setBusinessno("");
            nicePopupDto.setMethodtype("");
        }

        if (request.getUserCertifyType().equals("30")) { // 아이핀 인증의 경우
            nicePopupDto.setMethodtype("");
            nicePopupDto.setReceivedata("");
        }

        // 요청 데이터 암호화 (Base64)
        String data = "";
        try {
            data = new ObjectMapper().writeValueAsString(nicePopupDto);
        } catch (Exception e) {
            mapper.updtStTo80(userCertifyDto.getUserCertifyNo());
            logger.warn("직렬화 실패", e);
            throw new HopsException(HopsCode.SYSTEM_ERROR); // 내부 에러
        }

        String encString = "";
        String integrityValue = "";
        try {
            AES256Util aes256Util = new AES256Util(keyValue, ivValue);
            // encrypt
            encString = aes256Util.encrypt(data);

            // 3-3. Hmac 무결성체크값(integrityValue) 생성 (sha-256)
            byte[] hmacSha256 = aes256Util.hmac256(hmacValue.getBytes(), encString.getBytes());
            integrityValue = Base64.getEncoder().encodeToString(hmacSha256);
        } catch (Exception e) {
            mapper.updtStTo80(userCertifyDto.getUserCertifyNo());
            logger.warn("암호화 중 실패", e);
            throw new HopsException(HopsCode.SYSTEM_ERROR); // 내부 에러
        }

        /* 4. return response */
        niceCirtificationResponse.setEncData(encString);
        niceCirtificationResponse.setIntegrityValue(integrityValue);
        niceCirtificationResponse.setTokenVersionId(tokenVersionIdValue);
        niceCirtificationResponse.setUserCertifyNo(userCertifyDto.getUserCertifyNo());

        logger.info(niceCirtificationResponse.toString());
        return niceCirtificationResponse;
    }

    public RegKeyResponse selfAuthRegKey(RegKeyRequest request) throws HopsException{
        logger.info("나이스 본인인증 키 등록 request: {}", request);

        /* 1. Request 담기 */
        GetNiceEncryptTokenRequest getNiceEncryptTokenRequest = new GetNiceEncryptTokenRequest();
        GetNiceEncryptTokenRequest.DataBody dataBody = new GetNiceEncryptTokenRequest.DataBody();
        dataBody.setReq_no(request.getRequestId());
        dataBody.setReq_dtim(request.getReqDt());
        getNiceEncryptTokenRequest.setDataBody(dataBody);
        String productId = request.getProductId();
        String accessToken = request.getAccessToken();
        String clientId = request.getClientId();

        /* 2. Nice API 호출 */
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        Date currentDate = new Date();
        long currentTimeStamp = currentDate.getTime()/1000;
        String getAuth = accessToken + ":" + currentTimeStamp + ":" + clientId;
        String setAuth = Base64.getEncoder().encodeToString(getAuth.getBytes());

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "bearer " + setAuth);
        headers.add("ProductID", productId);
        HttpEntity<GetNiceEncryptTokenRequest> req = new HttpEntity<>(getNiceEncryptTokenRequest, headers);
        logger.info("NICE Request(Header): {}", headers);

        GetNiceEncryptTokenResponse.DataBody resp;
        try {
            ResponseEntity<GetNiceEncryptTokenResponse> httpResp = restTemplate.exchange(encryptTokenUrl, HttpMethod.POST, req, GetNiceEncryptTokenResponse.class);
            logger.info("NICE Response: {}", httpResp);
            resp = Objects.requireNonNull(httpResp.getBody()).getDataBody();
        } catch (Exception e) {
            logger.error("NICE ERROR", e);
            throw new HopsException(HopsCode.NETWORK_ERROR);
        }

        String tokenVersionId;
        String tokenVal;
        String siteCode;
        if (resp.getRsp_cd().equals("P000")) {
            tokenVersionId = resp.getToken_version_id();
            tokenVal = resp.getToken_val();
            siteCode = resp.getSite_code();
        } else {
            logger.error("[RegKey] 암호화토큰 요청 실패: {} ", resp.getRes_msg());
            throw new HopsException(HopsCode.NETWORK_ERROR);
        }

        /* 3. 대칭키 생성 */
        String val = request.getReqDt().trim() + request.getRequestId().trim() + tokenVal.trim();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new HopsException(HopsCode.PARSING_ERROR);
        }
        md.update(val.getBytes());
        byte[] arrHashValue = md.digest();
        String resultVal = Base64.getEncoder().encodeToString(arrHashValue);
        int resultLength = resultVal.trim().length();
        String key;
        String iv;
        String hmacKey;
        try {
            key = resultVal.substring(0, 16);
            iv = resultVal.substring(resultLength - 16, resultLength);
            hmacKey = resultVal.substring(0, 32);
        } catch (Exception e) {
            logger.error("ERROR(substring)", e);
            throw new HopsException(HopsCode.FORMAT_ERROR); // substring 동작 에러(자릿수)
        }
        logger.info("대칭키: {},", resultVal);

        /* 4. Response 반환 */
        RegKeyResponse response = new RegKeyResponse();
        response.setKey(key);
        response.setIv(iv);
        response.setHmacKey(hmacKey);
        response.setSiteCode(siteCode);
        response.setTokenVersionId(tokenVersionId);
        return response;
    }

    @Override
    public NiceCertificateAuthResponse userJoinCertificateAuth(NiceCertificateAuthRequest request) throws HopsException {

        NiceCertificateAuthResponse niceCertificateAuthResponse = new NiceCertificateAuthResponse();

        ValidUtil.validNull(request, "request");

        /* 1. 대칭키 생성 정보 조회 */
        String keyValue = "";
        String ivValue = "";
        String hmacValue = "";
        String siteCodeValue = "";
        String tokenVersionIdValue = "";

        if (request.getUserCertifyType().equals("10")) { // 핸드폰 본인인증이라면
            try {
                keyValue = mapper.getServiceInfo(KEY_ID_PHONE);
                ivValue = mapper.getServiceInfo(IV_ID_PHONE);
                hmacValue = mapper.getServiceInfo(HMAC_ID_PHONE);
                siteCodeValue = mapper.getServiceInfo(SITE_CODE_ID_PHONE);
                tokenVersionIdValue = mapper.getServiceInfo(TOKEN_VERSION_ID_PHONE);
            } catch (Exception e) {
                mapper.updtStTo80(request.getUserCertifyNo());
                logger.warn("DB error (GetServiceInfo)", e);
                throw new HopsException(HopsCode.DATABASE_ERROR); // DB error
            }
        } else if(request.getUserCertifyType().equals("30")) { // 아이핀 본인인증이라면
            try {
                keyValue = mapper.getServiceInfo(KEY_ID_IPIN);
                ivValue = mapper.getServiceInfo(IV_ID_IPIN);
                hmacValue = mapper.getServiceInfo(HMAC_ID_IPIN);
                siteCodeValue = mapper.getServiceInfo(SITE_CODE_ID_IPIN);
                tokenVersionIdValue = mapper.getServiceInfo(TOKEN_VERSION_ID_IPIN);
            } catch (Exception e) {
                mapper.updtStTo80(request.getUserCertifyNo());
                logger.warn("DB error (GetServiceInfo)", e);
                throw new HopsException(HopsCode.DATABASE_ERROR); // DB error
            }
        }

        // 대칭키 생성에 필요한 값들의 발급시간이 50분 초과되었을 경우
        if (ValidUtil.isEmpty(keyValue) || ValidUtil.isEmpty(ivValue)
                || ValidUtil.isEmpty(hmacValue) || ValidUtil.isEmpty(hmacValue)
                || ValidUtil.isEmpty(siteCodeValue) || ValidUtil.isEmpty(tokenVersionIdValue)){
            mapper.updtStTo80(request.getUserCertifyNo());
            logger.info("본인인증 시간 50분 초과");
            throw new HopsException(HopsCode.CERTIFICATE_TIME_ERROR); // 본인인증 시간이 초과되었습니다.
        }

        AES256Util aes256Util = new AES256Util(keyValue, ivValue);
        /* 2. 요청데이터 복호화 */
        String resData = aes256Util.decrypt(request.getCiperText());

        /* 3. string > Object (Jackson) */
        NiceAuthDto niceAuthDto;
        try {
            niceAuthDto = new ObjectMapper().readValue(resData, NiceAuthDto.class);
        } catch (Exception e) {
            mapper.updtStTo80(request.getUserCertifyNo());
            logger.warn("인증데이터 복호화 중 에러", e);
            throw new HopsException(HopsCode.DEC_ERROR); // 내부에러
        }

        /* 4. 인증 데이터 처리 */

        UserCertifyDto userCertifyDto = new UserCertifyDto();
        userCertifyDto.setUserCertifyNo(request.getUserCertifyNo());
        userCertifyDto.setCertifySt("50"); // 인증확인
        userCertifyDto.setBirthday(niceAuthDto.getBirthdate());
        if (niceAuthDto.getNationalinfo().equals("0")) { // 내국인이라면
            userCertifyDto.setDomesticYn("Y");
        } else if (niceAuthDto.getNationalinfo().equals("1")) { // 외국인이라면
            userCertifyDto.setDomesticYn("N");
        }
        String decodeData = "";
        try {
            decodeData = URLDecoder.decode(niceAuthDto.getUtf8_name(), "UTF-8");
        } catch (Exception e) {
            mapper.updtStTo80(request.getUserCertifyNo());
            logger.warn("문자열 변환 에러", e);
            throw new HopsException(HopsCode.SYSTEM_ERROR); // 내부에러
        }
        userCertifyDto.setUserName(decodeData);
        String ci = "";
        if (request.getUserCertifyType().equals("10")) { // 휴대폰 본인인증이라면

            if (!niceAuthDto.getResultcode().equals("0000")) {
                mapper.updtStTo80(request.getUserCertifyNo());
                logger.error("인증데이터 불일치");
                throw new HopsException(HopsCode.CERTIFICATE_ERROR); //본인인증이 실패했습니다.
            }

            // 4-1. 인증 확인 처리
            ci = niceAuthDto.getCi();
            userCertifyDto.setCi(niceAuthDto.getCi());
            userCertifyDto.setDi(niceAuthDto.getDi());
            userCertifyDto.setCertifyConfirmNo(niceAuthDto.getResponseno());
            userCertifyDto.setMobileNo(niceAuthDto.getMobileno());
            if (niceAuthDto.getGender().equals("0")) {
                userCertifyDto.setGenderCd("20"); // 여성
            } else if (niceAuthDto.getGender().equals("1")) {
                userCertifyDto.setGenderCd("10"); // 남성
            }
        } else if (request.getUserCertifyType().equals("30")) { // 아이핀 본인인증이라면

            if (!niceAuthDto.getResultcode().equals("1")) {
                mapper.updtStTo80(request.getUserCertifyNo());
                logger.error("인증데이터 불일치");
                throw new HopsException(HopsCode.CERTIFICATE_ERROR); //본인인증이 실패했습니다.
            }

            ci = niceAuthDto.getCoinfo1();
            userCertifyDto.setCi(niceAuthDto.getCoinfo1());
            userCertifyDto.setDi(niceAuthDto.getDupinfo());
            userCertifyDto.setCertifyConfirmNo("0");
            if (niceAuthDto.getGendercode().equals("0")) {
                userCertifyDto.setGenderCd("20"); // 여성
            } else if (niceAuthDto.getGendercode().equals("1")) {
                userCertifyDto.setGenderCd("10"); // 남성
            }
        }

        try {
            mapper.updateUserCertify(userCertifyDto); // HC_USER_CERTIFY
        } catch (Exception e) {
            mapper.updtStTo80(request.getUserCertifyNo());
            logger.warn("DB error (UpdtHcUserCertify)", e);
            throw new HopsException(HopsCode.DATABASE_ERROR); // DB error
        }

        /* 5. 기가입 회원 여부 조회 */
        Integer commonUserNo = 0;

        try {
            commonUserNo = mapper.getAlreadyCommonUserInfo(ci);
        } catch (Exception e) {
            logger.error("기존회원 정보 중 오류");
            throw new HopsException(HopsCode.DATABASE_ERROR); // DB error
        }
        if (ValidUtil.isEmpty(commonUserNo)) {
            commonUserNo = 0;
        }

        /* 6. 기존회원 기가입 여부 조회 : HC_USER */
        UserDto user;
        try {
            user = mapper.getAlreadyUserInfo(ci);
        } catch(Exception e) {
            logger.error("기존회원 정보 중 오류");
            throw new HopsException(HopsCode.DATABASE_ERROR); // DB error
        }

        /* 4. return response  */
        niceCertificateAuthResponse.setUserCertifyNo(request.getUserCertifyNo());
        niceCertificateAuthResponse.setCertifySuccessYn("Y");
        niceCertificateAuthResponse.setCommonUserNo(commonUserNo);
        niceCertificateAuthResponse.setBirthday(niceAuthDto.getBirthdate());
        niceCertificateAuthResponse.setGender(niceAuthDto.getGender().equals("1") ? "남" : "여");

        if(!ValidUtil.isEmpty(user) ) {
            niceCertificateAuthResponse.setCommonUserNo(0);
            niceCertificateAuthResponse.setAddress(user.getAddress());
            niceCertificateAuthResponse.setAddressDetail(user.getAddressDetail());
            niceCertificateAuthResponse.setUserId(user.getUserId());
            niceCertificateAuthResponse.setUserName(user.getUserName());
            niceCertificateAuthResponse.setZipCd(user.getZipCd());
            niceCertificateAuthResponse.setEmail(user.getEmail());
            niceCertificateAuthResponse.setPhoneNo(user.getPhoneNo());
            niceCertificateAuthResponse.setBirthday(user.getBirthday());
            niceCertificateAuthResponse.setMobileNo(user.getMobileNo());
            niceCertificateAuthResponse.setCustomerName(user.getCustomerName());
        }

        niceCertificateAuthResponse.setMobileNo(niceAuthDto.getMobileno());
        niceCertificateAuthResponse.setUserCi(niceAuthDto.getCi());
        logger.debug("niceCertificateAuthResponse: {}", niceCertificateAuthResponse);

        return niceCertificateAuthResponse;
    }
}
