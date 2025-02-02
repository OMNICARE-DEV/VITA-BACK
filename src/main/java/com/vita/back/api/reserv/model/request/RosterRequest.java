package com.vita.back.api.reserv.model.request;

import lombok.Data;

@Data
public class RosterRequest {
	private String commonUserNo = "";
    private int userNo = 0;
    private String customerId = "";
    private String userId = "";
    private String rosterName = "";
    private String email = "";
    private String birthday = "";
    private String employNo = "";
    private String genderCd = "";
    private String mobileNo = "";
    private String phoneNo = "";
    private String department = "";
    private String jobType = "";
    private String domesticYn = "";
    private String vipYn = "";
    private String checkupDivCd = "";
    private int companySupportAmount;
    private int companySupportFamilyCount;
    private int companySupportFamilyExceptEmployee;
    private String companySupporType = "";
    private String nhisTargetYn = "";
    private String specialCheckupYn = "";
    private String nighttimeTargetYn = "";
    private String etcCheckupText = "";
    private String specialCheckupText = "";
    private String beforeWorkTestYn = "";
    private String beforeWorkTestText = "";
    private String visitCheckupTargetYn = "";
    private String checkupStartDt = "";
    private String checkupEndDt = "";
    private String vaccineText = "";
    private String privacyAgreeYn = "";
    private String serviceAgreeYn = "";
    private String infoShareAgreeYn = "";
    private String sensitiveInfoAgreeYn = "";
    private int checkupProposNo;
    private String employRelationType = "";
    private String relationEmployNo = "";
    private String relationRosterName = "";
    private String relationBirthday = "";
    private String relationMobileNo = "";
    private String policyYear = "";
    private String regAdminId = "";
    private String rosterMgmtType = "";
    private int superCheckupRosterNo;
    private String nationality = "";
    private String nationalityCd = "";
    private String memoContents = "";
    private String regAdminName = "" ;
    private int checkupRosterNo;
    private String merchantShipYn = "N";
    /** 설문 평가 대상자 여부 */
    private String emotionalTestYN = "N";

    // 임직원 미등록 대상자 추가
    private String unRegEmployYN = "N";
    private String relationEmployGender = "";
    private String relationEmployEmail = "";
    private String relationEmployJobtype = "";
    private String relationEmployPhone = "";
    private String relationEmployDepartment = "";
    // 일괄대상자 등록 여부, 경로 표시 추가
    private String rosterBatchYN = "";
    private String lastPath = "";
    private String transCustomerYN = "";
    /** 마음 검진 대상자 여부
     * 00: 대상자 아님
     * 10: 마음검진24
     * 11: 마음검진43 */
    private String mindTestSt = "00";
    /* 대상자 영문명 */
    private String rosterEnglishName = "";


    private String loginType = "";
}