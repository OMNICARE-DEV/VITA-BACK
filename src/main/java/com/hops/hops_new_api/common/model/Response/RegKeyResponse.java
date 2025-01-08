package com.hops.hops_new_api.common.model.Response;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class RegKeyResponse {
    private String key;
    private String iv;
    private String hmacKey;
    private String siteCode;
    private String tokenVersionId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }
}
