package com.vita.back.api.service;

import com.vita.back.api.model.request.ReservRequest;
import com.vita.back.api.model.response.ReservResponse;
import com.vita.back.common.exception.VitaException;

public interface ReservService {
	public ReservResponse processReserv(ReservRequest params) throws VitaException;
}
