package com.vita.back.api.reserv.model.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class ConnRegReservRequest {
	private String customerId = "";
    private String partnerCenterId = "";
    private Integer checkupProductNo;
    private Integer reservNo;
    private String centerReservId="";
    private String deferCheckupReservHopeDay="";
    private String deferCheckupReservPartnerCenterId="";
    private List<String> deferCheckupReservTestItemList = new ArrayList<>();
    private List<String> deferCheckupReservAddTestItemList = new ArrayList<>();
    private int settlePrice = 0;
    private String customerIdByCenter = ""; //협력병원측 고객사ID
    private String policyYear = ""; //정책연도
    private int checkupRosterNo = 0; //검진대상자번호
    private String partnerCenterIdByCenter = ""; //
    private String productIdByCenter = ""; //
    private String checkupDivCd = ""; //검진구분코드
    private int superCheckupRosterNo = 0; //상위검진대상자번호
    private int selfPayAmount = 0; //본인결제금액
    private int companySupportAmount = 0; //회사지원금액
    private int onpayAmount = 0; //온라인결제금액
    private int offpayExpectAmount = 0; //현장결제예정금액
    private String requestContents = ""; //예약메모
    private String reserv1stHopeDay = ""; //예약1희망일자
    private String reserv2ndHopeDay = ""; //예약2희망일자
    private String reservHopeTime = "";
    private String resultReceiveWayCd = ""; //결과수신방법코드
    private String forceReservYN = ""; //강제예약여부
    private String address = ""; //주소
    private String zipCd = ""; //우편번호
    private String addressDetail = ""; //주소상세
    private String rosterName = ""; //성명
    private String mobileNo = ""; //연락처
    private String phoneNo = ""; //연락처
    private String birthday = ""; //생년월일
    private String genderCd = ""; //성별
    private String domesticYN = ""; //내국인여부
    private String specialCheckupYN = ""; //특수검진여부
    private String userDi = ""; //본인인증 DI
    private String userCi = ""; //본인인증 CI
    private String employNo = ""; //사번
    private String department = ""; //부서명
    private String employName = ""; //직원명
    private String employRelationType = ""; //직원관계타입
    private String email = ""; //이메일
    private String holdReservYN = ""; // 예약보류사용여부
    private String deferReservHopeDay = ""; // 연기예약희망일자
    private List<TestItemList> checkupItemList = new ArrayList<>();
    private List<ReplaceCheckupItemList> replaceItemList = new ArrayList<>();
    private List<TestItemList> testItemList = new ArrayList<>();
    private String reservDay = "";
    private String policyId = "";
    /* 2023-07-20 추가 */
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
    private String checkupItemCd = "";
    private String vipYN = "N";
    private String transCustomerYN = "N";
    private String preAcceptYN  = "N";
    private String deferType = "";
    private String deferItem = "";
    private String reservRequestContents = "";
    private String userId = "";
    private String migYN = "";
    private String reservFloor = "";
    private String customAmountYN = "";
    private int customCompanyAmount;
    private int customSelfAmount;
    private List<String> merchantShipList = new ArrayList<>();
    private List<String> reCheckupPkgList = new ArrayList<>();
    private String reCheckupYN = "";
    private String reservIdByCenter = "";
    private String reservFixYN="";
    private String smsYN="";
    /** 출장검진 여부 */
    private String team;
    /** 일반검진 HPC310 추가여부 */
    private String gnrlYN;
    private String premiumCapaYN = "N";
    private String familyBirth = "";
    /* 국적 코드 */
    private String nationalCd = "";
    /* 준비물 우편번호 */
    private String suppliesZipCd;
    /* 준비물 주소 */
    private String suppliesAddress;
    /* 준비물 상세 주소 */
    private String suppliesAddressDetail;
    /* 대상자 영문명 */
    private String rosterEnglishName;

    @Getter
    @Setter
    @ToString
    public static class ReplaceCheckupItemList {
        private String testItemCd = "";
        private String pkgCd = "";
        private String replaceItemCd = "";
        private String replacePkgCd = "";
        private String revertYN = "N";
        private int choiceGroupNo = 0;
    }

    @Getter
    @Setter
    @ToString
    public static class TestItemList {
        private String itemDivCd = "";
        private String testItemCd = "";
        private int choiceGroupNo = 0;
        private int choiceBundleNo = 0;
        private int addTestPrice = 0;
        private String deferCheckupYN = "";
        private String pkgCd = "";
        private String nonPriceYN = "";
        private String customPriceYN = "";
        private int customCompanyPrice;
        private int customSelfPrice;
    }
}