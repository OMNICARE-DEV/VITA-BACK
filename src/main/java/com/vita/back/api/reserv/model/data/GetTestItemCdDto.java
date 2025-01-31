package com.vita.back.api.reserv.model.data;

import lombok.Data;

@Data
public class GetTestItemCdDto {
	private String checkupItemCd = "";
	private String partnerCenterId = "";
	private String testItemCd = "";
	private int addTestPrice;
	private int checkupProductNo;
	private int choiceGroupNo;
	private String nonPriceYn = "N";
	private String customPriceYn = "N";
	private int customTotalPrice;
	private int customCompanyPrice;
	private int customSelfPrice;
	private String pkgCd = "";
}