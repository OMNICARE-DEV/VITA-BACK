package com.hops.hops_new_api.common.model.Response;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class NiceCertificateAuthResponse {
    private String certifySuccessYn = "";
    private int userCertifyNo;
    private int userNo;
    private int commonUserNo;

    private String mobileNo;
    private String userCi;
    
    private String customerName;
    private String userId;
    private String address;
    private String addressDetail;
    private String phoneNo;
    private String zipCd;
    private String email;
    private String userName;
    private String birthday; 

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }
}
