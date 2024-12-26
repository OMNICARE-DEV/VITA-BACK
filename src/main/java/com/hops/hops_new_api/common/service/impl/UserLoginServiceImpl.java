package com.hops.hops_new_api.common.service.impl;

import com.hops.hops_new_api.common.controller.UserLoginController;
import com.hops.hops_new_api.common.exception.HopsCode;
import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.mapper.UserLoginMapper;
import com.hops.hops_new_api.common.model.Request.UserLoginRequest;
import com.hops.hops_new_api.common.model.Response.UserLoginResponse;
import com.hops.hops_new_api.common.model.data.CustomerMapDto;
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

        try {
            userLoginResponse.setLoginMapList(mapper.getCommonUser(request));
            logger.info(userLoginResponse.toString());
            logger.info(Integer.toString(userLoginResponse.getLoginMapList().size()));
            boolean loginSuccess = userLoginResponse.getLoginMapList().size() > 1;

            if (loginSuccess) {
                return userLoginResponse;
            } else {
                throw new HopsException(HopsCode.FAIL_LOGIN);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new HopsException(HopsCode.FAIL_LOGIN);
        }

    };
}
