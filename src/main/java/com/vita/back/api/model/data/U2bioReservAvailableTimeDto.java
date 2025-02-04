package com.vita.back.api.model.data;

import java.util.List;

import lombok.Data;

@Data
public class U2bioReservAvailableTimeDto {
	private List<AppointmentTimes> appointmentTimes;

    @Data
    public static class AppointmentTimes {
        private String appointmentTime;
    }
}