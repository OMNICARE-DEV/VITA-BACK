package com.hops.hops_new_api.common.service.impl;

import com.hops.hops_new_api.common.exception.HopsCode;
import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.mapper.UserLoginMapper;
import com.hops.hops_new_api.common.model.Request.UserLoginRequest;
import com.hops.hops_new_api.common.model.Response.UserLoginResponse;
import com.hops.hops_new_api.common.model.data.CustomerMapDto;
import com.hops.hops_new_api.common.service.UserLoginService;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    private final UserLoginMapper mapper;

    public UserLoginServiceImpl(UserLoginMapper mapper) {
        this.mapper = mapper;
    }


    public UserLoginResponse userLogin(UserLoginRequest request) throws HopsException {

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        try {
            userLoginResponse = mapper.getCommonUser(request);

            boolean loginSuccess = userLoginResponse.getLoginMapList().size() > 1;

            if (loginSuccess) {
                return userLoginResponse;
            } else {
                throw new HopsException(HopsCode.FAIL_LOGIN);
            }
        }catch (Exception e){
            throw new HopsException(HopsCode.FAIL_LOGIN);
        }

    };
}
