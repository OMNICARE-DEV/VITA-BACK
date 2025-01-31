package com.vita.back.api.reserv.model.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GetRemainCapaDto {
	private String partnerCenterId = "";
	private String checkupPlaceId = "";
	private int testEquipNo;
	private String startDay = "";
	private String endDay = "";
	private String capaDayTime = "";
	private String capaDay = "";
	private String capaTime = "";
	private String remainCapa = "";
	private List<String> holidayList = new ArrayList<>();
}