package com.vita.back.api.reserv.model.data;

import lombok.Data;

@Data
public class CheckupProductDto {
	private int checkupProductNo;
	private String reservStartDt = "";
	private String reservEndDt = "";
	private String checkupStartDt = "";
	private String checkupEndDt = "";
	private String centerProductId = "";
	private int settlePrice;
	private String customerId = "";
	private String policyYear = "";
	private String centerPlaceId = "";
	private String migYn = "N";
	private int productIndex = 0;
	private int systemUsageFee = 0;
	private int checkupPrice = 0;
	private String centerProductNo;
	private String checkupProductTitle;
	private String specialProductIdByCenter;
}