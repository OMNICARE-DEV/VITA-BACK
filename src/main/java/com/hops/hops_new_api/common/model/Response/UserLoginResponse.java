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

    private List<CustomerMapDto> loginMapList = new ArrayList<>();
    private List<UserDto> userDtoList = new ArrayList<>();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }

    @Getter
    @Setter
    public static class UserDto {

        private String userId;
        private String userName;
        private String mobile;
        private String birthday;
        private String gender;
        private String loginPassword;
        private String email;
        private String address;
        private String addressDetail;
        private String zipCode;
        private String serviceTermsOfUse;
        private String privatePolicy;
        private String smsGuideReceiveYn;
        private String smsEventReceiveYn;
        private String emailGuideReceiveYn;
        private String emailEventReceiveYn;
        private String joinDt;
        private String userSt;
        private String customerId;
        private String employNo;
        private String department;
        private String jobPosition;
        private String phoneNo;
        private String userCi;
        private String userDi;
        private String lastPasswordChangeDt;
        private String agreeTermsList;
        private String pwdResetDt;
        private String marketingAgrYn;
        private String dormantYn;
        private String loginDt;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
        }

    }

    @Getter
    @Setter
    public static class CustomerMapDto {

        private String commonUserNo;
        private String userNo;
        private String customerId;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
        }

    }
}
