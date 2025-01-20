package com.vita.back.api.model.response;


import lombok.Data;

@Data
public class NiceCirtificationResponse {
    private String encData;
    private String integrityValue;
    private String tokenVersionId;
    private int userCertifyNo;
}
