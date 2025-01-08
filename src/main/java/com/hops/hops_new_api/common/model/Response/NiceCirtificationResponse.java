package com.hops.hops_new_api.common.model.Response;


import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class NiceCirtificationResponse {
    private String encData = "";
    private String integrityValue = "";
    private String tokenVersionId = "";
    private int userCertifyNo;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }
}
