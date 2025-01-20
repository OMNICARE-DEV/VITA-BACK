package com.vita.back.api.model.request;

import lombok.Data;

@Data
public class NiceCertificationRequest {
    private String returnUrl;
    private String userCertifyDiv;
    private String userCertifyType;
}
