package com.vita.back.api.model.data;

import lombok.Data;

@Data
public class U2bioPatientDto {
	private String patientNo = ".";
    private String name;
    private String socialNumber;
    private String birthDate;
    private String gender;
    private String cellNumber = "";
    private String phoneNumber = "";
    private String eMail = "";
    private String zoneCode = "";
    private String roadAddress = "";
    private String addressDetail = "";
}