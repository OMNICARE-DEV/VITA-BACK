package com.vita.back.api.reserv.model.data;

import lombok.Data;

@Data
public class PartnerCenterDto {
	private String partnerCenterId = "";
    private String partnerCenterName = "";
    private String ceoName = "";
    private String phoneNo = "";
    private String guidePhoneNo = "";
    private String fax = "";
    private String homePageUrl = "";
    private String resultReceiveWayCd = "";
    private String corpNo = "";
    private String area = "";
    private String addrress = "";
    private String addrressDetail = "";
    private String zipCd = "";
    private String specialCheckupYn = "";
    private String specialCheckupCountMgmtYn = "";
    private String nhisBillYn = "";
    private String onpayYn = "";
    private String colonoscopyLimitYn = "";
    private String reservSmsServiceYn = "";
    private String mainImageUrl = "";
    private String companyLogoUrl = "";
    private int minWaitingDayCount = 0;
    private String mapUrl = "";
    private String apiNetworkingYn = "";
    private String regDt = "";
    private String partnerCenterSt = "";
    private String partnerCenterType = "";
    private String note = "";
    private String salesAdminId = "";
    private String adminId = "";
    private String reserv2ndAbleYn = "";
    private int colonoscopyLimitAge = 0;
    private int sleepColonoscopyLimitAge = 0;
    private int gastroscopyLimitAge = 0;
    private int sleepGastroscopyLimitAge = 0;
    private String areaCd = "";
    private String capaMgmtType = "";
    private String centerIdByCenter = "";
    private String emailResultReceiveYn = "";
    private String postResultReceiveYn = "";
    private String visitResultReceiveYn = "";
    private String epostResultReceiveYn = "";
    private String onlineResultReceiveYn = "";
    private String parcelResultReceiveYn = "";
    private String partnerCenterDisplayYn = "Y";
    private String checkupDeferPermitYn = "Y";
    private String partnerCenterAlias = "";
    private String corporateYn = "N";
    private String corporateOrderNo = "";
}