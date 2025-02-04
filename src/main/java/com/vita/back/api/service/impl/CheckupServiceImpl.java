package com.vita.back.api.service.impl;

import com.vita.back.api.mapper.CheckupMapper;
import com.vita.back.api.model.data.CustomerDto;
import com.vita.back.api.model.response.*;
import com.vita.back.api.service.CheckupService;
import com.vita.back.common.exception.VitaException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = { Exception.class, VitaException.class })
public class CheckupServiceImpl implements CheckupService {
	
	private final CheckupMapper checkupMapper;
	
	@Override
	public CustomerResponse selectCustomerList(int commonUserNo) {
		CustomerResponse response = new CustomerResponse();
		List<CustomerDto> customerList = checkupMapper.selectCustomerList(commonUserNo);
		response.setCustomerList(customerList);
		return response;
	}
}
