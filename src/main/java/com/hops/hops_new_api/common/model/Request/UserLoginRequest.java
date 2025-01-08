package com.hops.hops_new_api.common.model.Request;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class UserLoginRequest {
    private String loginType; //10:통합아이디로그인 20:사번로그인 21:사업장아이디로그인
    private String userId;
    private String customerId;
    private String employNo;
    private String loginPassword;
    private String userCertifyNo;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }
}
