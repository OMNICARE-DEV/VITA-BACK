package com.vita.back.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vita.back.api.model.Constant;
import com.vita.back.api.model.VitaResponse;
import com.vita.back.api.model.request.ReservRequest;
import com.vita.back.api.model.response.CustomerResponse;
import com.vita.back.api.model.response.ReservResponse;
import com.vita.back.api.service.CheckupService;
import com.vita.back.api.service.ReservService;
import com.vita.back.common.exception.VitaException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "AUTH", description = "예약관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/reserv")
public class ReservController {
	
	private final ReservService reservService;
	private final CheckupService checkupService;
	
    @Operation(summary = "검진 예약", description = "검진 예약")
    @PostMapping("/process-reserv")
    public VitaResponse<?> processReserv(@RequestBody ReservRequest request) throws VitaException {
    	ReservResponse response = reservService.processReserv(request);
    	return new VitaResponse<>(Constant.SUCCESS, response);
    }
    
    @Operation(summary = "고객사 조회", description = "사용자가 소속된 고객사 조회")
    @GetMapping("/select-customer-list")
    public VitaResponse<?> selectCustomerList(@RequestParam(value = "commonUserNo") int commonUserNo) throws VitaException {
    	CustomerResponse response = checkupService.selectCustomerList(commonUserNo);
    	return new VitaResponse<>(Constant.SUCCESS, response);
    }
}
