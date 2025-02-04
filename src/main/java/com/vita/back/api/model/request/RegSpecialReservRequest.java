package com.vita.back.api.model.request;

import lombok.Data;

@Data
public class RegSpecialReservRequest {
	private int reservNo = 0;
    private String customerId = "";
    private String adminReqYn = "";
    private String address = "";
    private String addressDetail = "";
    private String zipCd = "";
    private String policyYear = "";
    private int checkupRosterNo = 0;
    private String partnerCenterId = "";
    private String checkupDivCd = "30";;
    private String reserv1stHopeDay = "";
    private String reservHopeTime = "";
    private String requestContents = "";
    private String resultReceiveWayCd = "";
    private String forceReservYn = "";
    private String phoneNo = "";
    private String mobileNo = "";
    private String email = "";
    private String userTermsAgreeList = "";
    private String batchReservYN = "N";
    private String smsYN = "Y";
    private String lastModifier = "";
    private String lastPath = "";
    private String reservRequestContents = "";
    private String ReservFloor = "";
    private String centerProductId = "";
}