package com.vita.back.api.service;

import com.vita.back.api.model.request.UpdatePayReservNoRequest;
import com.vita.back.common.exception.VitaException;

public interface PayService {

	void updatePayReservNo(UpdatePayReservNoRequest updatePayReservNoRequest) throws VitaException;

}
