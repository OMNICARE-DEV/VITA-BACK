package com.hops.hops_new_api.common.model.Request;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class RegCommonUserRequest {

    private String userCertifyNo;
    private String mobileNo;
    private String phoneNo;
    private String email;
    private String address;
    private String addressDetail;
    private String zipCd;
    private String userName;
    private String birthday;
    private String loginPassword;
    private String domesticYn;
    private String userId;
    private boolean serviceTermsOfUse;
    private boolean privacyPolicy;
    private boolean marketingAgree;



    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }
}
