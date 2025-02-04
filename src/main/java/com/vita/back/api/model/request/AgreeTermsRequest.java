package com.vita.back.api.model.request;

import lombok.Data;

@Data
public class AgreeTermsRequest {
	private int checkupRosterNo;
	private int reservNo;
	private int userNo;
	private String mobileNo;
	private String employRelationType = "00";
	private String firstServiceInfoId = "3001";
	private String secondServiceInfoId = "3002";
	private String thirdServiceInfoId = "3003";
	private String firstRequiredConsent = "N";
	private String secondRequiredConsent = "N";
	private String thirdSelectiveConsent = "N";
	private String agreePath;
	private String agreeDt;
}