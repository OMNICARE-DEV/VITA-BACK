package com.vita.back.api.reserv.model.request;

import lombok.Data;

@Data
public class U2bioReservAvailableDateRequest {
	private String startDate;
    private String endDate;
    private String testCodes;
    private String classCode = "SC";
    private String groupPatientId = "-1";
    private String groupId = "-1";
    private String promotionId = "-1";
    private String amPm = "AM";
}