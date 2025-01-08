package com.hops.hops_new_api.common.model.data;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class UserDto {

    private String customerName;
    private String userId;
    private String address;
    private String addressDetail;
    private String zipCode;
    private String phoneNo;
    private String mobileNo;
    private String birthday;
    private String userName;
    private String email;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }

}