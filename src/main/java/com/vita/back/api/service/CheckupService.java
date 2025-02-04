package com.vita.back.api.service;

import com.vita.back.api.model.response.CustomerResponse;

public interface CheckupService {
	CustomerResponse selectCustomerList(int commonUserNo);
	
}
