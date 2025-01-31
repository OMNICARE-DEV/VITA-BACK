package com.vita.back.api.pay.service;

import com.vita.back.api.pay.model.UpdatePayReservNoRequest;
import com.vita.back.common.exception.VitaException;

public interface PayService {

	void updatePayReservNo(UpdatePayReservNoRequest updatePayReservNoRequest) throws VitaException;

}
