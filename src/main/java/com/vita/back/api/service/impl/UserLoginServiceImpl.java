package com.vita.back.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.vita.back.api.mapper.UserLoginMapper;
import com.vita.back.api.model.data.CommonUserDto;
import com.vita.back.api.model.data.CustomerMapDto;
import com.vita.back.api.model.data.UserCertifyDto;
import com.vita.back.api.model.data.UserDto;
import com.vita.back.api.model.data.UserIdDupCheckDto;
import com.vita.back.api.model.request.RegCommonUserRequest;
import com.vita.back.api.model.request.UserLoginRequest;
import com.vita.back.api.model.response.RegCommonUserResponse;
import com.vita.back.api.model.response.UserLoginResponse;
import com.vita.back.api.service.UserLoginService;
import com.vita.back.common.exception.VitaCode;
import com.vita.back.common.exception.VitaException;
import com.vita.back.common.util.AES256Util;
import com.vita.back.common.util.ValidUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserLoginServiceImpl implements UserLoginService {
    private final UserLoginMapper mapper;

    public UserLoginServiceImpl(UserLoginMapper mapper, CacheServiceImpl cacheService) {
        this.mapper = mapper;
    }

    /** 통합회원 로그인 */
    public int userLogin(UserLoginRequest request) throws VitaException {
        log.info("통합회원 로그인 userLogin");
        /* 필수값 체크*/
        ValidUtil.validNull(
            request.getUserId(),
            request.getLoginPassword()
        );

        int commonUserNo = 0;
        try {
            /* 공통회원 db조회 */
            Integer commonUserNoChk = mapper.getCommonUserNo(request);
            if(commonUserNoChk != null) {
                commonUserNo = commonUserNoChk;
            }

            log.info("userLogin response: {}",commonUserNo);
            if(commonUserNo == 0){
                throw new VitaException(VitaCode.FAIL_LOGIN);
            }else {
                return commonUserNo;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new VitaException(VitaCode.FAIL_LOGIN);
        }

    }

    //통합회원 B2C신규등록
    @Override
    @Transactional(rollbackFor = {VitaException.class})
    public int regB2CUser(int commonUserNo) throws VitaException {
        log.info("통합회원 B2C신규등록 regB2CUser");
        //기존 B2C유저등록 되어있는지 체크
        int regB2CUserCount = mapper.checkB2CUser(commonUserNo);

        //등록이 안되어 있으면 등록
        if(regB2CUserCount == 0) {
            UserDto b2CUser = mapper.getCommonUser(commonUserNo);

            if(ValidUtil.isEmpty(b2CUser)){
                throw new VitaException(VitaCode.INVALID_PARAMETER);
            }else{
                //B2C고객사 강제 지정
                b2CUser.setCustomerId("C000008035");
                //개인 회원 등록
                regB2CUserCount = mapper.regB2CUser(b2CUser);
                log.info("regB2CUser 등록: {}",b2CUser);

                //고객사맵핑 등록(개인회원)
                CustomerMapDto customerMapDto = new CustomerMapDto();
                customerMapDto.setCommonUserNo(commonUserNo);
                customerMapDto.setUserNo(b2CUser.getUserNo());
                customerMapDto.setCustomerId(b2CUser.getCustomerId());

                List<CustomerMapDto> customerMaps = new ArrayList<>();
                customerMaps.add(customerMapDto);

                log.info("regCustomerMap 등록: {}",customerMaps);
                int regCustomerMapCount = mapper.regCustomerMap(customerMaps);
                if(regCustomerMapCount == 0) {
                    throw new VitaException(VitaCode.REG_B2C_USER_ERROR);
                }
            }
        }

        return regB2CUserCount;
    }

    //통합로그인 아이디 중복확인
    @Override
    public int userIdDupCheck(UserLoginRequest request) throws VitaException {
        log.info("통합로그인 아이디 중복확인 userIdDupCheck");
        /* 필수값 체크*/
        ValidUtil.validNull(
                request.getUserId(),
                request.getUserCertifyNo()
        );

        UserIdDupCheckDto userIdDupCheckDto = new UserIdDupCheckDto();
        userIdDupCheckDto.setUserId(request.getUserId());
        userIdDupCheckDto.setUserCi(mapper.getUserCi(request.getUserCertifyNo()));

        int commonDupIdCount = 0;
        try {
            commonDupIdCount = mapper.getCommonUserIdDupCheck(userIdDupCheckDto);

            log.info("userIdDupCheck response: {}",commonDupIdCount);

            return commonDupIdCount;
        }catch (Exception e){
            e.printStackTrace();
            throw new VitaException(VitaCode.FAIL_LOGIN);
        }
    }

    //통합회원 등록
    @Override
    @Transactional(rollbackFor = {VitaException.class})
    public RegCommonUserResponse regCommonUser(RegCommonUserRequest request) throws VitaException {
        log.info("통합회원 등록 regCommonUser");
        RegCommonUserResponse regCommonUserResponse = new RegCommonUserResponse();

        /* 필수값 체크*/
        ValidUtil.validNull(
                request.getUserId(),
                request.getLoginPassword(),
                request.getUserName(),
                request.getBirthday(),
                request.getMobileNo(),
                request.getUserCertifyNo()
        );

        //아이디 중복체크
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUserId(request.getUserId());
        userLoginRequest.setUserCertifyNo(request.getUserCertifyNo());

        int commonDupIdCount = userIdDupCheck(userLoginRequest);

        if(commonDupIdCount > 0){
            throw new VitaException(VitaCode.ID_DUPLICATE_ERROR);
        }

        //본인인증 조회
        UserCertifyDto userCertifyDto = mapper.getCertifyInfoByKey(request.getUserCertifyNo());
        log.info("userCertifyDto response: {}",userCertifyDto);

        if(ValidUtil.isEmpty(userCertifyDto)){
            throw new VitaException(VitaCode.CERTIFICATE_TIME_ERROR);
        }
        // 본인인증값 사용완료 처리
        mapper.updtCertifySt(request.getUserCertifyNo());

        // 만 14세이하 가입불가 처리
        int americanAge = ValidUtil.getAmericanAge(userCertifyDto.getBirthday());
        if (americanAge <= 14 ) { // 만 14세 이하라면
            throw new VitaException(VitaCode.JOIN_AGE_ERROR);
        }

        //회원가입 이력찾기
        UserIdDupCheckDto userCiDupCheckDto = new UserIdDupCheckDto();
        userCiDupCheckDto.setUserCi(userCertifyDto.getCi());
        int userCiCount = mapper.getCommonUserCount(userCiDupCheckDto);
        if (userCiCount > 0){
            throw new VitaException(VitaCode.ALREADY_JOIN_USER);
        }else {
            try {
                CommonUserDto regCommonUserDto = new CommonUserDto();
                regCommonUserDto.setUserId(request.getUserId());
                regCommonUserDto.setUserName(request.getUserName());
                regCommonUserDto.setMobileNo(request.getMobileNo());
                regCommonUserDto.setBirthday(request.getBirthday());
                regCommonUserDto.setGenderCd(userCertifyDto.getGenderCd());
                regCommonUserDto.setLoginPassword(request.getLoginPassword());
                regCommonUserDto.setEmail(request.getEmail());
                regCommonUserDto.setAddress(request.getAddress());
                regCommonUserDto.setAddressDetail(request.getAddressDetail());
                regCommonUserDto.setZipCd(request.getZipCd());
                regCommonUserDto.setUserSt("00");
                regCommonUserDto.setUserCi(userCertifyDto.getCi());
                regCommonUserDto.setUserDi(userCertifyDto.getDi());
                regCommonUserDto.setServiceTermsOfUse(request.isServiceTermsOfUse() ? "Y" : "N");
                regCommonUserDto.setPrivacyPolicy(request.isPrivacyPolicy() ? "Y" : "N");
                regCommonUserDto.setMarketingAgrYn(request.isMarketingAgree() ? "Y" : "N");
                regCommonUserDto.setDomesticYn(userCertifyDto.getDomesticYn());
                String userTermsAgreeList = "1,2" + (request.isMarketingAgree() ? ",3" : "");//1,2번은 필수
                regCommonUserDto.setAgreeTermsList(userTermsAgreeList);
                mapper.joinCommonUser(regCommonUserDto); // 회원 등록

                log.info("regCommonUserDto response: {}",regCommonUserDto);

                //기존 회원 매핑
                boolean successRegCustomerMap = mappingCustomerUser(regCommonUserDto.getCommonUserNo());
                if(successRegCustomerMap) {
                    regCommonUserResponse.setJoinSuccess(true);
                }else{
                    throw new VitaException(VitaCode.DATABASE_ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 강제로 롤백
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new VitaException(VitaCode.DATABASE_ERROR);
            }
        }
        log.info("regCommonUserResponse response: {}",regCommonUserResponse);
        return regCommonUserResponse;
    }

    //로그인 완료
    @Override
    public UserLoginResponse getUserLoginResponse(int commonUserNo) throws VitaException {
        //TODO 비번 변경 90일/ 휴면계정 체크필요
        log.info("로그인 완료 getUserLoginResponse");
        try {
            mapper.updateCommonUserLoginDt(commonUserNo);
            mapper.updateUserLoginDt(commonUserNo);
        }catch (Exception e){
            e.printStackTrace();
            throw new VitaException(VitaCode.DATABASE_ERROR);
        }


        try {
            UserLoginResponse userLoginResponse = mapper.getUserLoginResponse(commonUserNo);

            String data = Integer.toString(commonUserNo);
            String loginDt = userLoginResponse.getLoginDt();
            String key = Integer.toString(commonUserNo);

            byte[] keyBytes = key.getBytes("UTF-8");
            if(keyBytes.length != 16) {
                key = "0".repeat(16-keyBytes.length) + commonUserNo;
            }
            log.info("암호화 key/iv"+key+"/"+loginDt);
            AES256Util aes256Util = new AES256Util(key, loginDt);
            String secretKey = aes256Util.encrypt(data);
            log.info("암호화 값"+secretKey);
            userLoginResponse.setSecretKey(secretKey);

            return userLoginResponse;
        }catch (Exception e){
            e.printStackTrace();
            throw new VitaException(VitaCode.INVALID_PARAMETER);
        }
    }

    //기존회원 통합회원 매핑
    @Override
    public boolean mappingCustomerUser(int commonUserNo) throws VitaException {
        log.info("기존회원 통합회원 매핑 mappingCustomerUser");

        UserDto b2CUser = mapper.getCommonUser(commonUserNo);

        List<CustomerMapDto> customerMaps = mapper.getMappingCustomerUser(b2CUser.getUserCi());

        log.info("mappingCustomerUser.mappingCustomerUsers response: {}", customerMaps);

        int mappingCustomerUserCount = customerMaps.size();
        int regCustomerUserCount = 0;
        if(mappingCustomerUserCount != 0) {
            regCustomerUserCount = mapper.regCustomerMap(customerMaps);
        }

        return mappingCustomerUserCount == regCustomerUserCount;
    }
}
