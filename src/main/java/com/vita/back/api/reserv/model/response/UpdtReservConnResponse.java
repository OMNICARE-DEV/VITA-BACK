package com.vita.back.api.reserv.model.response;

import lombok.Data;

@Data
public class UpdtReservConnResponse {
	private String reservSt = "";
	private int reservNo;
	private String centerReservId = "";
	private String reservDay = "";
	// 연기검진
	private int deferCheckupReservNo;
	private String reservDt = ""; // 연기검진 확정일자
	private String lastPath = "";
	private String lastModifier = "";
	private String multiYn = "N";
}