package com.hops.hops_new_api.common.model.Request;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class NiceCertificateAuthRequest {
    private int userCertifyNo;
    private String ciperText;
    private String userCertifyType;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }
}
