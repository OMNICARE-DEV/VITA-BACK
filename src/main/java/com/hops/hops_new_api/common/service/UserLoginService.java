package com.hops.hops_new_api.common.service;

import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.model.Request.UserLoginRequest;
import com.hops.hops_new_api.common.model.Response.UserLoginResponse;

public interface UserLoginService {
    public UserLoginResponse userLogin(UserLoginRequest request) throws HopsException;
}
