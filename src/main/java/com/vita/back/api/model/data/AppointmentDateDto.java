package com.vita.back.api.model.data;

import java.util.List;

import lombok.Data;

@Data
public class AppointmentDateDto {
	private String appointmentDate;
    private String enableToAppoint;
    private String disableReasonCode;
    private String disableReasonMsg;
    private List<AppointmentTimes> appointmentTimes;

    @Data
    public static class AppointmentTimes {
        private String appointmentTime;
    }
}