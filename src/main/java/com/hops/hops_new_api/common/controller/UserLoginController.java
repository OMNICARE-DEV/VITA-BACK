package com.hops.hops_new_api.common.controller;

import com.hops.hops_new_api.common.exception.HopsCode;
import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.model.Constant;
import com.hops.hops_new_api.common.model.HopsResponse;
import com.hops.hops_new_api.common.model.Request.UserLoginRequest;
import com.hops.hops_new_api.common.service.UserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserLoginController {

    private final UserLoginService service;

    public UserLoginController(UserLoginService service) {
        this.service = service;
    }

    // 통합로그인 아이디 로그인
    @RequestMapping(value = "/commonIdLogin")
    public HopsResponse commonIdLogin(@RequestBody UserLoginRequest request) throws HopsException {
        return new HopsResponse<>(Constant.SUCCESS, service.userLogin(request));
    }
}
