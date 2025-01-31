package com.vita.back.api.reserv.model.data;

import lombok.Data;

@Data
public class CheckupPlaceDto {
	private String partnerCenterId = "";
	private String centerPlaceId = "";
	private int checkupProductNo;
	private String policyYear = "";
}