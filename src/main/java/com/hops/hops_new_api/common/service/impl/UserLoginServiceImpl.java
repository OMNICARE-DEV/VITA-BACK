package com.hops.hops_new_api.common.service.impl;

import com.hops.hops_new_api.common.controller.UserLoginController;
import com.hops.hops_new_api.common.exception.HopsCode;
import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.mapper.UserLoginMapper;
import com.hops.hops_new_api.common.model.Request.UserLoginRequest;
import com.hops.hops_new_api.common.model.Response.UserLoginResponse;
import com.hops.hops_new_api.common.model.util.ValidUtil;
import com.hops.hops_new_api.common.service.UserLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
            request.getUserPassword()
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

    };
}
