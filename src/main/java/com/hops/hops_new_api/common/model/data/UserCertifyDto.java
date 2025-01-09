package com.hops.hops_new_api.common.model.data;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class UserCertifyDto {
    private int userCertifyNo;
    private String userCertifyDiv;
    private int userNo;
    private String mobileNo;
    private String checkupRosterNo;
    private String userCertifyType;
    private String certifyConfirmNo;
    private String regDt;
    private String certifySt;
    private String ci;
    private String di;
    private String userName;
    private String birthday;
    private String genderCd;
    private String domesticYn;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }
}
