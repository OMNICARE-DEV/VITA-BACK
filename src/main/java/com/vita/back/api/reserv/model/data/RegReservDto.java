package com.vita.back.api.reserv.model.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RegReservDto {
	private int reservNo; // pk, auto increment
	private int checkupProductNo; // req
	private int checkupRosterNo; // req
	private String partnerCenterId = ""; // req
	private String checkupPlaceId = "";
	private String checkupDivCd = ""; // req - 일반검진, 특수검진, 종합검진, 출장검진, 기타검진
	private int selfPayAmount; // req // 본인결제금액
	private int companySupportAmount; // req // 회사지원금액
	private int onpayAmount; // req // 온라인결제금액
	private int offpayExpectAmount; // req // 현장결제예정금액
	private int superReservNo;
	private String note = ""; // req
	private String reservRegDt = ""; // sysdate
	private String reserv1stHopeDay = ""; // req
	private String reserv2ndHopeDay = ""; // req
	private String reservTime = "";
	private String reserv2ndAbleYn = "";
	private String requestContents = ""; // req
	private String nhisResultUsesAgreeYn = ""; // req
	private String privacyUsesAgreeYn = ""; // req
	private String hcShareAgreeYn = ""; // req
	private String companyItselfAgreeYn = ""; // req
	private String privacyDisplayAgreeYn = ""; // req
	private String hcAgreeYn = ""; // req
	private String resultReceiveWayCd = ""; // req
	private String forceReservYn = ""; // req
	private String reservDay = ""; // request
	private String reservSt = ""; // 예약상태 10:등록 20:예약전송 50:예약승인 60:변경완료 80:예약취소 90:예약실패
	private String reservConfirmDt = ""; // 예약확정일시
	private String reservChangeContents = "";
	private String reservChangeType = "";
	private String reservChangeDt = "";
	private String reservChangeSt = "";
	private int originReservNo;
	private int selfpaySedationAmt; // 본인결제수면금액
	private int companySupportSedationAmt; // 회사지원수면금액
	private String mobileNo = "";
	private String phoneNo = "";
	private String address = "";
	private String zipCd = "";
	private String addressDetail = "";
	private String email = "";
	private String centerReservId = "";
	private String agreeTermsList = "";
	private String batchReservYn = "N";
	private String smsYn = "Y";
	private String lastModifier = ""; // 최종수정자
	private String lastPath = ""; // 유입경로(어드민/피씨/모바일)
	private String customerId = "";
	private String policyYear = "";
	private String domesticYn = "";
	private String rosterDi = "";
	private String specialCheckupYn = "";
	/* 2023-07-20 추가 */
	private String pregnancyYn = "";
	private String possiblePregnancyYn = "";
	private String feedingYn = "";
	private String mensesYn = "";
	private String anticoagulantYn = "";
	private String melituriaYn = "";
	private String arteriotonyYn = "";
	private String medicationText = "";
	private String nephropathyYn = "";
	private String cardiopathyYn = "";
	private String renalFailureYn = "";
	private String peritonealDialysisYn = "";
	private String hemodialysisYn = "";
	private String caseHistoryText = "";
	private String vipYn = "";
	private String transCustomerYn = "";
	private String preAcceptYn = "";
	private String kicsLastModifier = "";
	private String reservFloor = "";
	private String customAmountYn = "N";
	private int customTotalAmount;
	private int customCompanyAmount;
	private int customSelfAmount;
	private String onlineConfirmYn = "N";
	private int productPrice;
	private int productIndex = 0;
	private String centerProductId = "";
	private String centerProductNo = "";
	private String reCheckupYn;
	private String genderCd = "";
	private String campaignRegNm = "";
	private String reservFixYn = "";
	private String bulkBatchNo;
	private String bulkRowNo;
	private String updateYn = "N";
	private String premiumCapaYn = "N";
	private String suppliesZipCd;
	private String suppliesAddress;
	private String suppliesAddressDetail;
	private String multiYn = "N";
	private String rosterName = "";
	private String birthDay = "";
	private String customerName = "";
	private String reservRequestContents = "";
	private String employRelationType = "";
	private String checkupProductTitle = "";
	private int settlePrice;
	private String migYn = "";   
}
