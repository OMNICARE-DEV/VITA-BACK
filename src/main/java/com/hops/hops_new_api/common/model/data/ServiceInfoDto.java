package com.hops.hops_new_api.common.model.data;

import lombok.Data;

@Data
public class ServiceInfoDto {
    private String serviceInfoId;
    private String infoValue;
    private String keyId;
    private String ivId;
    private String hmacId;
    private String siteCodeId;
    private String tokenVersionId;
}
