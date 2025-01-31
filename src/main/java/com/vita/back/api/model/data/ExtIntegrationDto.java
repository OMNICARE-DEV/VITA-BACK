package com.vita.back.api.model.data;

import lombok.Data;

@Data
public class ExtIntegrationDto {
	private String partnerCenterId = "";
    private String itgUrl = "";
    private String itgId = "";
    private String itgPwd = "";
    private String itgTarget = "";
}