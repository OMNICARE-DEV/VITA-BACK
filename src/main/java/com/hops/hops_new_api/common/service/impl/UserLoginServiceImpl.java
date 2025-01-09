package com.hops.hops_new_api.common.service.impl;

import com.hops.hops_new_api.common.controller.UserLoginController;
import com.hops.hops_new_api.common.exception.HopsCode;
import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.mapper.UserLoginMapper;
import com.hops.hops_new_api.common.model.Request.RegCommonUserRequest;
import com.hops.hops_new_api.common.model.Request.UserLoginRequest;
import com.hops.hops_new_api.common.model.Response.RegCommonUserResponse;
import com.hops.hops_new_api.common.model.Response.UserLoginResponse;
import com.hops.hops_new_api.common.model.data.CommonUserDto;
import com.hops.hops_new_api.common.model.data.UserCertifyDto;
import com.hops.hops_new_api.common.model.data.UserDto;
import com.hops.hops_new_api.common.model.data.UserIdDupCheckDto;
import com.hops.hops_new_api.common.model.util.ValidUtil;
import com.hops.hops_new_api.common.service.UserLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    private static final Logger logger = LoggerFactory.getLogger(UserLoginServiceImpl.class);

    private final UserLoginMapper mapper;

    public UserLoginServiceImpl(UserLoginMapper mapper) {
        this.mapper = mapper;
    }


    public UserLoginResponse userLogin(UserLoginRequest request) throws HopsException {

        UserLoginResponse userLoginResponse = new UserLoginResponse();

        /* 필수값 체크*/
        ValidUtil.validNull(
            request.getUserId(),
            request.getLoginPassword()
        );

        try {
            //공통회원 db조회
            userLoginResponse.setLoginMapList(mapper.getCommonUser(request));

            logger.info("commonIdLogin response: {}",userLoginResponse);

            return userLoginResponse;
        }catch (Exception e){
            e.printStackTrace();
            throw new HopsException(HopsCode.FAIL_LOGIN);
        }

    }

    public int userIdDupCheck(UserLoginRequest request) throws HopsException {

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

            logger.info("userIdDupCheck response: {}",commonDupIdCount);

            return commonDupIdCount;
        }catch (Exception e){
            e.printStackTrace();
            throw new HopsException(HopsCode.FAIL_LOGIN);
        }
    }

    @Override
    @Transactional(rollbackFor = {HopsException.class})
    public RegCommonUserResponse regCommonUser(RegCommonUserRequest request) throws HopsException {

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
            throw new HopsException(HopsCode.ID_DUPLICATE_ERROR);
        }

        //본인인증 조회
        UserCertifyDto userCertifyDto = mapper.getCertifyInfoByKey(request.getUserCertifyNo());
        logger.info("userCertifyDto response: {}",userCertifyDto);

        if(ValidUtil.isEmpty(userCertifyDto)){
            throw new HopsException(HopsCode.CERTIFICATE_TIME_ERROR);
        }
        // 본인인증값 사용완료 처리
        mapper.updtCertifySt(request.getUserCertifyNo());

        // 만 14세이하 가입불가 처리
        int americanAge = ValidUtil.getAmericanAge(userCertifyDto.getBirthday());
        if (americanAge <= 14 ) { // 만 14세 이하라면
            throw new HopsException(HopsCode.JOIN_AGE_ERROR);
        }

        //회원가입 이력찾기
        UserIdDupCheckDto userCiDupCheckDto = new UserIdDupCheckDto();
        userCiDupCheckDto.setUserCi(userCertifyDto.getCi());

        int userCiCount = mapper.getCommonUserCount(userCiDupCheckDto);
        if (userCiCount > 0){
            throw new HopsException(HopsCode.ALREADY_JOIN_USER);
        }else {
            try {
                CommonUserDto regCommonUser = new CommonUserDto();
                regCommonUser.setUserId(request.getUserId());
                regCommonUser.setUserName(request.getUserName());
                regCommonUser.setMobileNo(request.getMobileNo());
                regCommonUser.setBirthday(request.getBirthday());
                regCommonUser.setGenderCd(userCertifyDto.getGenderCd());
                regCommonUser.setLoginPassword(request.getLoginPassword());
                regCommonUser.setEmail(request.getEmail());
                regCommonUser.setAddress(request.getAddress());
                regCommonUser.setAddressDetail(request.getAddressDetail());
                regCommonUser.setZipCd(request.getZipCd());
                regCommonUser.setUserSt("00");
                regCommonUser.setUserCi(userCertifyDto.getCi());
                regCommonUser.setUserDi(userCertifyDto.getDi());
                regCommonUser.setServiceTermsOfUse(request.isServiceTermsOfUse() ? "Y" : "N");
                regCommonUser.setPrivacyPolicy(request.isPrivacyPolicy() ? "Y" : "N");
                regCommonUser.setMarketingAgrYn(request.isMarketingAgree() ? "Y" : "N");
                regCommonUser.setDomesticYn(userCertifyDto.getDomesticYn());
                String userTermsAgreeList = "1,2" + (request.isMarketingAgree() ? ",3" : "");//1,2번은 필수
                regCommonUser.setAgreeTermsList(userTermsAgreeList);
                logger.info("regCommonUser response: {}",regCommonUser);
                mapper.joinCommonUser(regCommonUser); // 회원 등록
                regCommonUserResponse.setJoinSuccess(true);
            } catch (Exception e) {
                e.printStackTrace();
                // 강제로 롤백
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new HopsException(HopsCode.DATABASE_ERROR);
            }
        }
        logger.info("regCommonUserResponse response: {}",regCommonUserResponse);
        return regCommonUserResponse;
    }

    ;
}
