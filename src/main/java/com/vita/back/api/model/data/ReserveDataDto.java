package com.vita.back.api.model.data;

import java.util.List;

import lombok.Data;

@Data
public class ReserveDataDto {
	/** U2Check 접수ID : U2Check 예약(접수) 생성시 생성되는 고유 Key 값 (예약생성시 -1 로 전달) */
    private int receptionId = -1;
    /** 수검자정보 */
    private U2bioPatientDto patientInfo;
    /** 검진년도(검진정책년도) */
    private int checkupYear;
    /** 검진구분코드 개인/기업 : 단독(SC), 공단포함(SCGG) */
    private String gumjinGubunCode = "SC";
    /** 검진구분명 : 종합검진, 공단검진 */
    private String gumjinGubunName = "종합검진";
    /** 종검세트 기본검사 */
    private List<BasisTest> basisTest;
    /** 선택검사 */
    private List<SelectionTest> selectionTest;
    /** 추가검사 */
    private List<AdditionalTest> additionalTest;
    /** 관계 */
    private String relationShips;
    /** 총결제금액 */
    private int totalPaymentAmount;
    /** 결제수단코드 0 : 미선택 / 1 : 후불 / 2 : 후불+당일 / 3 : 당일수납 */
    private String paymentMethod = "0";
    /** 사업장아이디 CustomerId */
    private String companyId;
    /** 사업장명 */
    private String companyDepartment;
    /** 결과지 수령방법
     * 1등기,2일반우편,3카카오톡,4메일,5헬스월릿,6방문(내원수령),7엘씨테크,8자택우편,9회사우편
     */
    private String receiptWayId;
    /** 공단검진 항목 포함여부 Y : 포함 / N : 포함X */
    private String includeGongdan = "N";
    /** 예약일자 yyyy-mm-dd */
    private String apptDate;
    /** 예약시간 hh:mm */
    private String apptTime;
    /** 사용자 Id -> API Auth 계정 */
    private String userId;
    /** 공단검진 수검 내원 시 결정 여부 : Y: 내원 시 결정, N: 기본값 */
    private String isUndecidedNhic = "N";
    /** 모바일 특이사항 */
    private String mobileRemark = "";
    /** 수검자 메모 */
    private String reserveMemo = "";
    //region [미사용항목]
    /** 옴니케어 미사용 항목 */
    private String groupPatientId = "-1";
    /** 예약취소 및 변경 가능 여부 코드 */
    private String possibleChangeCode = "";
    /** 예약취소 및 변경 가능 여부 사유 */
    private String possibleChangeMsg = "";
    /** 개인정보 활용동의 여부 */
    private String isPersonalConsent = "Y";
    private NhicInfo nhicInfo = new NhicInfo();
    private String[] subAppt = new String[]{};
    private String[] article = new String[]{};
    private String promotionId = "-1";
    //endregion
    
    @Data
    public static class BasisTest {
        /** 검진항목명 */
        private String testName;
        /** 검진항목코드 */
        private String testCode;
        /** 회사 지원금 */
        private int basisSupportPaymentAmount = 0;
        /** U2Check 오더ID
         * U2Check 오더 생성시 생성되는 고유 Key 값 (예약생성시 -1 로 전달)
         */
        private int itemId = -1;
    }

    @Data
    public static class SelectionTest {
        /** 검진항목명 */
        private String testName;
        /** 검진항목코드 */
        private String testCode;
        /** 선택검사그룹명
         * 종검생성시 선택검사 생성할 때 정의한 선택검사그룹명 : U2Check과 약속한 선택검사그룹명을 보내줘야 함
         */
        private String selectionName;
        /** U2Check 오더ID */
        private int itemId = -1;
    }
    
    @Data
    public static class AdditionalTest {
        /** 검진항목명 */
        private String testName;
        /** 검진항목코드 */
        private String testCode;
        private int testPaymentAmount = 0;
        /** 회사 지원금 */
        private int testSupportPaymentAmount = 0;
        /** U2Check 오더ID
         * U2Check 오더 생성시 생성되는 고유 Key 값 (예약생성시 -1 로 전달)
         */
        private int itemId = -1;
    }

    @Data
    public static class NhicInfo {
        private String nhicDetail1 = "";
        private String nhicDetail2 = "";
        private String nhicDetail3 = "";
    }
}