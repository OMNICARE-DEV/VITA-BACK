package com.vita.back.api.service;

import com.vita.back.api.model.request.RegCommonUserRequest;
import com.vita.back.api.model.request.UserLoginRequest;
import com.vita.back.api.model.response.RegCommonUserResponse;
import com.vita.back.api.model.response.UserLoginResponse;
import com.vita.back.common.exception.VitaException;

public interface UserLoginService {
    public int userLogin(UserLoginRequest request) throws VitaException;

    public int regB2CUser(int commonUserNo) throws VitaException;

    public int userIdDupCheck(UserLoginRequest request) throws VitaException;;

    public RegCommonUserResponse regCommonUser(RegCommonUserRequest request) throws VitaException;

    public UserLoginResponse getUserLoginResponse(int commonUserNo) throws VitaException;

    public boolean mappingCustomerUser(int commonUserNo) throws VitaException;
}
