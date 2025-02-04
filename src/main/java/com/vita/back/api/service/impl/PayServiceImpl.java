package com.vita.back.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vita.back.api.mapper.PayMapper;
import com.vita.back.api.model.request.UpdatePayReservNoRequest;
import com.vita.back.api.service.PayService;
import com.vita.back.common.exception.VitaCode;
import com.vita.back.common.exception.VitaException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = { Exception.class, VitaException.class })
public class PayServiceImpl implements PayService {

	private final PayMapper mapper;
	
	public PayServiceImpl(PayMapper mapper) {
	  	this.mapper = mapper;
  	}
	
	public void updatePayReservNo(UpdatePayReservNoRequest request) throws VitaException {
		log.debug("updatePayReservNo request: {}", request);
        if (request.getReservNo() == 0  || request.getPayNo() == 0){
            log.warn("필수값 누락");
            throw new VitaException(VitaCode.REQUIRED_VALUE_FAIL); // 필수값 누락
        }

        Integer mappedReservNo = mapper.selectPayMappedReserv(request.getPayNo());

        if (mappedReservNo == -1){
            try{
                mapper.updatePayReservNo(request);
                mapper.insertPayHist(request.getPayNo());
            } catch (Exception e){
                log.warn("DB error (updatePayReservNo)", e);
                throw new VitaException(VitaCode.DATABASE_ERROR); // DB error
            }
        }
	}

}
