package com.vita.back.api.model.response;

import lombok.Data;

@Data
public class RegKeyResponse {
    private String key;
    private String iv;
    private String hmacKey;
    private String siteCode;
    private String tokenVersionId;
}
