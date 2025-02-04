package com.vita.back.api.model.data;

import lombok.Data;

@Data
public class DeferReservTestItemDto {
	private String partnerCenterId = "";
	private int checkupProductNo;
	private String checkupItemCd = "";
	private String testItemCd = "";
	private String itemDivCd = "";
	private int choiceGroupNo;
	private int choiceBundleNo;
	private String addCheckupYn = "";
	private int deferCheckupReservNo;
	private int deferReservMomentAmount;
	private int reservNo;
	private int deferCheckupReservHistNo;
	private String deferReservTestItemSt = "";
	private String holdYn = "";
	private String target = "";
	private String groupCd = "";
	private String centerDeferReservId = "";
	private String pkgCd = "";
}