package com.vita.back.api.reserv.service;

import com.vita.back.api.reserv.model.request.ReservRequest;
import com.vita.back.api.reserv.model.response.ReservResponse;
import com.vita.back.common.exception.VitaException;

public interface ReservService {
	public ReservResponse processReserv(ReservRequest params) throws VitaException;
}
