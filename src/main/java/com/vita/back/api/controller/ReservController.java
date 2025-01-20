package com.vita.back.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vita.back.api.model.Constant;
import com.vita.back.api.model.VitaResponse;
import com.vita.back.common.exception.VitaException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "AUTH", description = "예약관련 API")
@RestController
@RequestMapping(value = "/reserv")
public class ReservController {
    @Operation(summary = "병원 조회", description = "병원 조회")
    @PostMapping("/partnerCenterList")
    public VitaResponse<?> partnerCenterList(@RequestParam(value="rosterNo") String request) throws VitaException {
        return new VitaResponse<> (Constant.SUCCESS, false);
    }
}
