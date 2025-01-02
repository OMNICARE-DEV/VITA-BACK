package com.hops.hops_new_api.common.model.Response;


import lombok.Data;

@Data
public class NiceCirtificationResponse {
    private String encData = "";
    private String integrityValue = "";
    private String tokenVersionId = "";
    private int userCertifyNo;

}
