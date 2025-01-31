package com.vita.back.api.reserv.model.data;

import lombok.Data;

@Data
public class DeferCheckupReservDto {
	private int deferCheckupReservNo = 0;
	private String partnerCenterId = "";
	private int reservNo = 0;
	private String centerDeferReservId = "";
	private String reservHopeDt = "";
	private String reservDt = "";
	private String reservRegDt = "";
	private String reservSt = "";
	private String deferReservChangeDt = "";
	private String deferReservChangeSt = "";
	private String reserv2ndHopeDay = "";
	private String reserv1stHopeDay = "";
	private String lastModifier = "";
	private String lastPath = "";
	private String kicsLastModifier = "";
}