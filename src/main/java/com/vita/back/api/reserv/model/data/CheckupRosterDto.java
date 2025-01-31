package com.vita.back.api.reserv.model.data;

import lombok.Data;

@Data
public class CheckupRosterDto {
	private String unRegEmployYn;
	private String mobileNo = "";
	private String phoneNo = "";
	private String email = "";
	private String rosterName = "";
	private String birthday = "";
	private String genderCd = "";
	private String domesticYn = "";
	private String specialCheckupYn = "";
	private String employNo = "";
	private String department = "";
	private String employRelationType = "";
	private String rosterDi = "";
	private String rosterCi = "";
	private String checkupStartDt = "";
	private String checkupEndDt = "";
	private String customerId = "";
	private String policyYear = "";
	private int superCheckupRosterNo;
	private int checkupRosterNo;
	private String vipYn = "";
	private String checkupDivCd = "";
	private String regAdminId = "";
	private String unRegEmployBirthday = "";
	private String unRegEmployEmail = "";
	private String unRegEmployMobile = "";
	private String unRegEmployJobtype = "";
	private String unRegEmployGender = "";
	private String unRegEmployNo = "";
	private String unRegEmployName = "";
	private String unRegEmployPhone = "";
	private String unRegEmployDepartment = "";
	/* 국적 코드 */
	private String nationalityCd = "";
	private String customerName = "";
	private String mobileNoYn = "";
	/* 대상자 영문명 */
	private String rosterEnglishName = "";
}