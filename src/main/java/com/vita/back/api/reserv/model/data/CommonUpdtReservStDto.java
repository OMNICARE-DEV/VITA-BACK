package com.vita.back.api.reserv.model.data;

import lombok.Data;

@Data
public class CommonUpdtReservStDto {
	private int reservNo = 0;
	private String reservSt = "";
	private String reservChangeSt = "";
	private String reservConfirmDt = "";
	private String centerReservId = "";
	private String reservChangeType = "";
	private String superReservNo = "";
	private String beforeReservSt = "";
	private String reservDay = "";
	private String lastModifier = "";
	private String lastPath = "";
	private String kicsLastModifier = "";
}