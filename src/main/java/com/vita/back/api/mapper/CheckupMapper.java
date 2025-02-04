package com.vita.back.api.mapper;

import com.vita.back.api.model.data.*;
import com.vita.back.api.model.request.AgreeTermsRequest;
import com.vita.back.api.model.request.CheckupRosterRequest;
import com.vita.back.api.model.request.ReservRequest;
import com.vita.back.api.model.request.UpdateRosterRequest;
import com.vita.back.api.model.response.UpdtReservConnResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface CheckupMapper {

	List<CustomerDto> selectCustomerList(int commonUserNo);

}
