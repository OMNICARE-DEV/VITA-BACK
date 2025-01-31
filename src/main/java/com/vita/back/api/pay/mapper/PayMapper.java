package com.vita.back.api.pay.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.vita.back.api.pay.model.UpdatePayReservNoRequest;

@Mapper
public interface PayMapper {
	Integer selectPayMappedReserv(int payNo);

	void updatePayReservNo(UpdatePayReservNoRequest request);

	void insertPayHist(int payNo);
}
