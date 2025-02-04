package com.vita.back.api.model.response;

import java.util.ArrayList;
import java.util.List;

import com.vita.back.api.model.data.CustomerDto;

import lombok.Data;

@Data
public class CustomerResponse {
	/** 사용자 소속 고객사 리스트 */
	private List<CustomerDto> customerList = new ArrayList<>();
}
