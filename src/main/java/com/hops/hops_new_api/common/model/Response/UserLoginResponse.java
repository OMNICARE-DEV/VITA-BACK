package com.hops.hops_new_api.common.model.Response;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserLoginResponse {

    private int commonUserNo;
    private String userName;
    private String genderCd;
    private int reservCount;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }

}
