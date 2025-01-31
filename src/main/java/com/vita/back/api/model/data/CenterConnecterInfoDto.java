package com.vita.back.api.model.data;

import lombok.Data;

@Data
public class CenterConnecterInfoDto {
	private String centerId = "";
	private String connecterId = "";
	private String connecterType = "";
	private String connecterUrl = "";
	private String connecterKey = "";
	private String connecterMainCenterId = "";
}