package com.vita.back.api.reserv.model.data;

import lombok.Data;

@Data
public class RegDeferReservValIdDto {
	private int reservNo;
	private String testItemCd = "";
	private int choiceGroupNo;
	private int choiceBundleNo;
	private String itemSt = "";
	private int deferCheckupReservNo;
}