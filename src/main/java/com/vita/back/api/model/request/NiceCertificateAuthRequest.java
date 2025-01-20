package com.vita.back.api.model.request;

import lombok.Data;

@Data
public class NiceCertificateAuthRequest {
    private int userCertifyNo;
    private String ciperText;
    private String userCertifyType;
}
