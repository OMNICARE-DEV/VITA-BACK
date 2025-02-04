package com.vita.back.api.model.data;

import lombok.Data;

@Data
public class GetRoomInfoDto {
	private String centerEquiproomId = "";
	private String equipTestName = "";
	private String partnerCenterId = "";
	private int testEquipNo;
	private String testItemCd = "";
}