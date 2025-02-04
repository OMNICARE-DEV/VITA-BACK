package com.vita.back.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vita.back.api.model.Constant;
import com.vita.back.api.model.VitaResponse;
import com.vita.back.api.model.request.ReservRequest;
import com.vita.back.api.model.response.ReservResponse;
import com.vita.back.api.service.ReservService;
import com.vita.back.common.exception.VitaException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "AUTH", description = "예약관련 API")
@RestController
@RequestMapping(value = "/reserv")
public class ReservController {
	
	private final ReservService service;
	
	public ReservController(ReservService service) {
		this.service = service;
	}
	
    @Operation(summary = "검진 예약", description = "검진 예약")
    @PostMapping("/process-reserv")
    public VitaResponse<?> processReserv(@RequestBody ReservRequest request) throws VitaException {
    	ReservResponse response = service.processReserv(request);
    	return new VitaResponse<>(Constant.SUCCESS, response);
    }
}
