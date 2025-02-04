package com.vita.back.api.service;

import com.vita.back.api.model.request.RegCommonUserRequest;
import com.vita.back.api.model.request.UserLoginRequest;
import com.vita.back.api.model.response.RegCommonUserResponse;
import com.vita.back.api.model.response.UserLoginResponse;
import com.vita.back.common.exception.VitaException;

public interface UserLoginService {
    int userLogin(UserLoginRequest request) throws VitaException;

    int userIdDupCheck(UserLoginRequest request) throws VitaException;

    RegCommonUserResponse insertCommonUser(RegCommonUserRequest request) throws VitaException;

    UserLoginResponse getUserLoginResponse(UserLoginRequest request) throws VitaException;

    boolean mappingCustomerUser(int commonUserNo) throws VitaException;
}
