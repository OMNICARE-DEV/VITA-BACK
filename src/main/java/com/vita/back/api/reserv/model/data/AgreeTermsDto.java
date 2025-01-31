package com.vita.back.api.reserv.model.data;

import lombok.Data;

@Data
public class AgreeTermsDto {
	private String agreePath;
    private String mobileNo;
    private int checkupRosterNo;
    private String firstConsent;
    private String secondConsent;
    private String thirdConsent;
}