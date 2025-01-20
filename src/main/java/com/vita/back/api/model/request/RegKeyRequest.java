package com.vita.back.api.model.request;

import lombok.Data;

@Data
public class RegKeyRequest {
    private String requestId;
    private String reqDt;
    private String certifyType;
    private String productId;
    private String accessToken;
    private String clientId;
}
