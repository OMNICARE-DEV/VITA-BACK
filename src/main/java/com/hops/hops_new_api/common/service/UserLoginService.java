package com.hops.hops_new_api.common.service;

import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.model.Request.RegCommonUserRequest;
import com.hops.hops_new_api.common.model.Request.UserLoginRequest;
import com.hops.hops_new_api.common.model.Response.RegCommonUserResponse;
import com.hops.hops_new_api.common.model.Response.UserLoginResponse;

public interface UserLoginService {
    public int userLogin(UserLoginRequest request) throws HopsException;

    public int regB2CUser(int commonUserNo) throws HopsException;

    public int userIdDupCheck(UserLoginRequest request) throws HopsException;;

    public RegCommonUserResponse regCommonUser(RegCommonUserRequest request) throws HopsException;

    public UserLoginResponse getUserLoginResponse(int commonUserNo) throws HopsException;

    public boolean mappingCustomerUser(int commonUserNo) throws HopsException;
}
