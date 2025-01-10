package com.hops.hops_new_api.common.model.data;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;


@Getter
@Setter
public class CustomerMapDto {

    private int commonUserNo;
    private int userNo;
    private String customerId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }
}
