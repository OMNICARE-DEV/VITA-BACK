package com.hops.hops_new_api.common.model.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {

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
    private String jobposition;
    private String phoneNo;
    private String userCi;
    private String userDi;
    private String lastPasswordChangeDt;
    private String agreeTermsList;
    private String pwdResetDt;
    private String marketingAgrYn;
    private String dormantYn;
    private String loginDt;

}
