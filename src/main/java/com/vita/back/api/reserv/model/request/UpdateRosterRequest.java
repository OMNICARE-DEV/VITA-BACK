package com.vita.back.api.reserv.model.request;

import lombok.Data;

@Data
public class UpdateRosterRequest {
	private Integer checkupRosterNo = 0;
    private String rosterName = "";
    private String genderCd = "";
    private String mobileNo = "";
    private String birthday = "";
    private String employNo = "";
    private String phoneNo = "";
    private String department = "";
    private String email = "";
    private String jobType = "";
    private String employRelationType = "";
    private String domesticYN = "";
    private String nationality = "";
    private String nationalityCd = "";
    private String vipYN = "";
    private String checkupDivCd = "";
    private String vaccineText = "";
    private String etcCheckupText = "";
    private String visitCheckupTargetYN = "";
    private String companySupportType = "";
    private Integer companySupportAmount;
    private Integer companySupportFamilyCount;
    private Integer companySupportChargeAmount;
    private String nhisTargetYN = "";
    private String specialCheckupYN = "";
    private String specialCheckupText = "";
    private String merchantShipYN = "";
    private String changeAdminId = "";
    private String productGroupNo = "";
    private String memoTitle = "";
    private String memoContents = "";
    private String regAdminame = "";
    private String page = "";
    private String rosterBatchYN = "";
    private String lastPath = "";
    private String emotionalTestYN =  "";
    private String otYN = "";
    /** 마음 검진 대상자 여부
     * 00: 대상자 아님
     * 10: 마음검진24
     * 11: 마음검진43 */
    private String mindTestSt = "00";
    private String memoChgYN = "N";
    /* 대상자 영문명 */
    private String rosterEnglishName;
}