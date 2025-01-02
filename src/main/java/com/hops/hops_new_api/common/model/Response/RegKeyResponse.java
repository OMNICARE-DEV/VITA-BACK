package com.hops.hops_new_api.common.model.Response;

import lombok.Data;

@Data
public class RegKeyResponse {
    private String key;
    private String iv;
    private String hmacKey;
    private String siteCode;
    private String tokenVersionId;
}
