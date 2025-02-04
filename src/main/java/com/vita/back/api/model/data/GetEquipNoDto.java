package com.vita.back.api.model.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GetEquipNoDto {
	private String partnerCenterId = "";
	private String testItemCd = "";
	private List<String> testItemStrList = new ArrayList<>();
}