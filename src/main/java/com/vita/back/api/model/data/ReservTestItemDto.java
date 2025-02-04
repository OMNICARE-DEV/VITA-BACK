package com.vita.back.api.model.data;

import lombok.Data;

@Data
public class ReservTestItemDto {
	private int reservNo;
	private String testItemCd = "";
	private String checkupItemCd = "";
	private int choiceGroupNo;
	private String addCheckupYn = "";
	private int selfPayAmount;
	private int deferCheckupReservNo;
	private String reservTestItemSt = "";
	private int onpayAmount;
	private int choiceBundleNo;
	private String itemDivCd = "";
	private String deferCheckupYn = "N";
	private String replaceItem = "";
	private String holdYn = "";
	private String nonPriceYn = "N";
	private String customPriceYn = "N";
	private int customTotalPrice;
	private int customCompanyPrice;
	private int customSelfPrice;
	private String pkgCd = "";
	private String testItemName = "";
	private int addTestPrice = 0;
}