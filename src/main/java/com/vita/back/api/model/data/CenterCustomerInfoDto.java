package com.vita.back.api.model.data;

import lombok.Data;

@Data
public class CenterCustomerInfoDto {
	private String customerIdByCenter = "";
	private String centerId = "";
	private String customerId = "";
	private int checkupRosterNo;
}