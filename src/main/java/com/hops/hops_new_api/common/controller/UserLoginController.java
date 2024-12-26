package com.hops.hops_new_api.common.controller;

import com.hops.hops_new_api.common.exception.HopsCode;
import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.model.Constant;
import com.hops.hops_new_api.common.model.HopsResponse;
import com.hops.hops_new_api.common.model.Request.UserLoginRequest;
import com.hops.hops_new_api.common.service.UserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AUTH", description = "로그인 등 인증관련 API")
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
    public HopsResponse commonIdLogin(@RequestBody UserLoginRequest request) throws HopsException {
        logger.info("UserLoginController.commonIdLogin request: {}", request);
        return new HopsResponse<>(Constant.SUCCESS, service.userLogin(request));
    }
}
