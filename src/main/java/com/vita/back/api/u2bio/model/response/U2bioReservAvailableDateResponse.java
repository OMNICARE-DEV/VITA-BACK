package com.vita.back.api.u2bio.model.response;

import java.util.List;

import com.vita.back.api.reserv.model.data.AppointmentDateDto;

import lombok.Data;

@Data
public class U2bioReservAvailableDateResponse {
	private List<AppointmentDateDto> appointmentDatesWithTimes;
}