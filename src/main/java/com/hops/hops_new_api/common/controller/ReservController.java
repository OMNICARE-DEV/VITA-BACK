package com.hops.hops_new_api.common.controller;

import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.model.Constant;
import com.hops.hops_new_api.common.model.HopsResponse;
import com.hops.hops_new_api.common.model.Request.NiceCertificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AUTH", description = "예약관련 API")
@RestController
@RequestMapping(value = "/reserv")
@Slf4j
public class ReservController {
    private static final Logger logger = LoggerFactory.getLogger(ReservController.class);

    @Operation(summary = "병원 조회", description = "병원 조회")
    @PostMapping("/partnerCenterList")
    public HopsResponse partnerCenterList(@RequestParam(value="rosterNo") String request) throws HopsException {
        logger.info("ReservController.partnerCenterList request: {}", request);
        return new HopsResponse<>(Constant.SUCCESS, false);//*-*-*-*-*-*-*-
    }
}
