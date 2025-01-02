package com.hops.hops_new_api.common.model.Request;

import lombok.Data;

@Data
public class NiceCertificationRequest {
    private String returnUrl = "";
    private String userCertifyDiv = "";
    private String userCertifyType = "";
}
