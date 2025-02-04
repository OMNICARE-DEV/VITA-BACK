package com.vita.back.api.model.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class ReservRequest {
	// Primitive 
	private int originReservNo;
    private int reservNo = 0;
    private int deferReservNo = 0; // 연기검진 번호
    private String checkupPlaceId = ""; // 검진장소 ID
    private String adminReqYn = "";
    private String address;
    private String addressDetail;
    private String zipCd;
    private String customerId = "";
    private String policyYear = "";
    private int checkupRosterNo = 0;
    private String partnerCenterId = "";
    private String deferPartnerCenterId = "";
    private int checkupProductNo = 0;
    private String checkupDivCd = "";
    private int superCheckupRosterNo = 0;
    private int selfPayAmount = 0;
    private int companySupportAmount = 0;
    private int onpayAmount = 0;
    private int offpayExpectAmount = 0;
    private String note = "";
    private String reserv1stHopeDay = "";
    private String reserv2ndHopeDay = "";
    private String reservHopeTime = "";
    private String requestContents = "";
    private String nhisResultUsesAgreeYn = "";
    private String privacyUsesAgreeYn = "";
    private String hcShareAgreeYn = "";
    private String companyItselfAgreeYn = "";
    private String privacyDisplayAgreeYn = "";
    private String hcAgreeYn = "";
    private String resultReceiveWayCd = "";
    private String forceReservYn = "";
    private String phoneNo = "";
    private String mobileNo = "";
    private String email = "";
    private String deferReservHopeDay = "";
    private String userTermsAgreeList = "";
    private String batchReservYN = "N";
    private String smsYN = "Y";
    private String companySupportType="";
    private String lastModifier = "";
    private String lastPath = "";
    private int deferCheckupReservNo = 0;
    private String pregnancyYN = "N";
    private String possiblePregnancyYN = "N";
    private String feedingYN = "N";
    private String mensesYN = "N";
    private String anticoagulantYN = "N";
    private String melituriaYN = "N";
    private String arteriotonyYN = "N";
    private String medicationText = "";
    private String nephropathyYN = "N";
    private String cardiopathyYN = "N";
    private String renalFailureYN = "N";
    private String peritionealDialysisYN = "N";
    private String hemodialysisYN = "N";
    private String caseHistoryText = "";
    private String vipYN = "N";
    private String transCustomerYN = "N";
    private String preAcceptYN = "N";
    private String deferItem = "";
    private String reservRequestContents = "";
    private String superReservYn = "N";
    private String deferCheckupItem = "";
    private String centerDeferReservId = "";
    private String target = "";
    private String reservFloor = "";
    private String customAmountYN = "";
    private int customTotalAmount;
    private int customCompanyAmount;
    private int customSelfAmount;
    private int multiReservSize = 0;
    private String centerProductId = "";
    private String reDeferCheckupYn = "";
    private String reservIdByCenter = "";
    private String deferReservHopeTime = "";
    private String deferReservFloor = "";
    private String campaignRegNm = "";
    private String partnerCenterIdByCenter = "";
    private String rosterName = "";
    private String userId = "";
    private String reservFixYn="N";
    private String deferReservMemo="";
    private int companySupportAddAmount = 0;
    private int selfPayAddAmount = 0;
    private String specialCheckupYN = "";
    private String premiumCapaYN = "N";
    private Integer companySupportSedationAmt = 0;
    private Integer selfPaySedationAmt = 0;
    /* 준비물 우편번호 */
    private String suppliesZipCd="";
    /* 준비물 주소 */
    private String suppliesAddress="";
    /* 준비물 상세 주소 */
    private String suppliesAddressDetail="";
    
    // Reference
    
    private List<CheckupItemList> checkupItemList = new  ArrayList<>();
    //private List<ReplaceCheckupItemList> replaceCheckupItemList = new ArrayList<>();
    //private Memo memo = new Memo();
    private RegRosterRequest regRosterRequest = new RegRosterRequest();
    private List<String> deferCheckupItemList = new ArrayList<>();
    private List<String> deferCheckupAddItemList = new ArrayList<>();
    private Integer payNo;
    private List<String> delSpecialCenterProductId = new ArrayList<>();
    private List<String> delSpecialCenterProductType = new ArrayList<>();
    private List<String> merchantShipList = new ArrayList<>();
    //private List<ConnDeferRegReservReq> connDeferRegReservReqList = new ArrayList<>();
    //private List<modifyIdDateList> modifyIdDateLists = new ArrayList<>();
    private List<String > reCheckupPkgItemList = new ArrayList<>();
    private List<String > addCheckupItemList = new ArrayList<>();
    private List<String > specialCenterProductId = new ArrayList<>();
    
    @Data
    public static class CheckupItemList {
        private String itemDivCd = ""; //10:기본,20:선택,30:추가
        private String checkupItemCd = "";
        private int choiceGroupNo = 0;
        private int choiceBundleNo = 0;
        private int selfPayAmount = 0;
        private int onpayAmount = 0;
        private String deferCheckupYN = "N";
        private String nonPriceYN = "";
        private String customPriceYN = "";
        private int customTotalPrice;
        private int customCompanyPrice;
        private int customSelfPrice;
        private String pkgCd;
        private String ReplaceItem = "";
        private String testItemCd;
    }
    
    @Data
    public static class RegRosterRequest {
        private String memoContents = "";
        private int userNo = 0;
        private String customerId = "";
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
        private int companySupportAmount = 0;
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
        private int checkupProposNo = 0;
        private String employRelationType = "";
        private String relationEmployNo = "";
        private String relationRosterName = "";
        private String relationBirthday = "";
        private String relationMobileNo = "";
        private String policyYear = "";
        private String regAdminId = "";
        private String rosterMgmtType = "";

        private String merchantShipYn = "N";

        // 임직원 미등록 대상자 추가
        private String unRegEmployYN = "N";
        private String relationEmployGender = "";
        private String relationEmployEmail = "";
        private String relationEmployJobtype = "";
        private String relationEmployPhone = "";
        private String relationEmployDepartment = "";
        private String transCustomerYN = "";

        private String lastPath = "";
    }
}
