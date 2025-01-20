package com.vita.back.api.controller;

import com.vita.back.api.model.Constant;
import com.vita.back.api.model.VitaResponse;
import com.vita.back.api.model.request.RegCommonUserRequest;
import com.vita.back.api.model.request.UserLoginRequest;
import com.vita.back.api.model.response.RegCommonUserResponse;
import com.vita.back.api.model.response.UserLoginResponse;
import com.vita.back.api.service.UserLoginService;
import com.vita.back.common.exception.VitaCode;
import com.vita.back.common.exception.VitaException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AUTH", description = "로그인/가입 API")
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserLoginController {
    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    private final UserLoginService service;

    public UserLoginController(UserLoginService service) {
        this.service = service;
    }

    // 통합로그인 아이디 로그인
    @Operation(summary = "로그인", description = "로그인 정보 조회")
    @PostMapping("/commonIdLogin")
    public VitaResponse commonIdLogin(@RequestBody UserLoginRequest request) throws VitaException {
        logger.info("UserLoginController.commonIdLogin request: {}", request);
        //Response
        UserLoginResponse userLoginResponse = new UserLoginResponse();

        //commonUserNO확인
        int commonUserNo = service.userLogin(request);

        int regB2CUserCount = 0;
        if(commonUserNo > 0){
            //개인회원 등록
            regB2CUserCount = service.regB2CUser(commonUserNo);
        }

        if(regB2CUserCount == 0){
            throw new VitaException(VitaCode.REG_B2C_USER_ERROR);
        }else{
            //추가 등록된 user mapping
            boolean successRegCustomerMap = service.mappingCustomerUser(commonUserNo);

            if(successRegCustomerMap) {
                userLoginResponse = service.getUserLoginResponse(commonUserNo);
            }else{
                throw new VitaException(VitaCode.DATABASE_ERROR);
            }
        }

        return new VitaResponse<>(Constant.SUCCESS, userLoginResponse);
    }

    // 통합로그인 아이디 중복체크
    @Operation(summary = "중복체크", description = "아이디 중복체크")
    @PostMapping("/commonIdDupCheck")
    public VitaResponse commonIdDupCheck(@RequestBody UserLoginRequest request) throws VitaException {
        logger.info("UserLoginController.commonIdDupCheck request: {}", request);
        return new VitaResponse<>(Constant.SUCCESS, service.userIdDupCheck(request));
    }

    // 통합로그인 회원가입
    @Operation(summary = "통합로그인 회원가입", description = "회원가입")
    @PostMapping("/regCommonUser")
    public VitaResponse regCommonUser(@RequestBody RegCommonUserRequest request) throws VitaException {
        logger.info("UserLoginController.regCommonUser request: {}", request);
        service.regCommonUser(request);
        return new VitaResponse<>(Constant.SUCCESS, service.regCommonUser(request));
    }

}
