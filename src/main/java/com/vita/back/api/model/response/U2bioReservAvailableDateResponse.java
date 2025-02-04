package com.vita.back.api.model.response;

import java.util.List;

import com.vita.back.api.model.data.AppointmentDateDto;

import lombok.Data;

@Data
public class U2bioReservAvailableDateResponse {
	private List<AppointmentDateDto> appointmentDatesWithTimes;
}