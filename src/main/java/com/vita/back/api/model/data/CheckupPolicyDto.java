package com.vita.back.api.model.data;

import lombok.Data;

@Data
public class CheckupPolicyDto {
	private String customerId = "";                     // 고객사아이디
    private String policyYear = "";                     // 정책연도
    private int personnel = 0;                          // 인원
    private int familySupportAmount = 0;                // 가족지원금액 (삭제예정)
    private String settleCycleCd = "";                  // 정산주기코드
    private String checkupDeferPermitYn = "";           // 검진연기허용여부
    private String productDisplayType = "";             // 상품표시타입 10:동일상품노출 20:하위상품노출 30:상위상품노출 50:전체상품노출 60:고객사지정
    private String reservStartDt = "";                  // 예약시작일시
    private String reservEndDt = "";                    // 예약종료일시
    private String checkupStartDt = "";                 // 검진시작일시
    private String checkupEndDt = "";                   // 검진종료일시
    private String familyCheckupStartDt = "";           // 가족검진시작일시
    private String familyCheckupEndDt = "";             // 가족검진종료일시
    private String checkupDivCd = "";                   // 검진구분코드
    private String colonoscopyLimitYn = "";             // (삭제예정)대장내시경제한여부
    private String visitCheckupYn = "";                 // 출장검진여부
    private String employCheckupYn = "";                // 채용검진여부
    private String vaccineText = "";                    // 백신텍스트
    private String workenvReportUrl = "";               // 작업환경측정서URL
    private String specialCheckupReqmUrl = "";          // 특수검진요청서URL
    private String specialCheckupAdminId = "";          // 특수검진운영자아이디
    private String nhisResultAllYn = "";                // 공단결과전체여부
    private String billCycleCd = "";                    // 청구주기코드
    private String invoiceIssueCd = "";                 // 계산서발행코드
    private String employRecheckupSupportType = "";     // 채용재검진지원타입 10:본인부담 20:회사부담
    private String nhisMinusYn = "";                    // 공단차감여부
    private String billTargetCd = "";                   // 청구대상코드
    private String billUniquenessGuide = "";            // 청구특이사항안내
    private byte[] checkupPolicyGuide = new byte[0];      // 검진정책안내
    private String note = "";                           // 비고
    private String regDt = "";                          // 등록일시
    private int companySupportAmount = 0;               // 회사지원금액
    private String etcCheckupText = "";                 // 기타검진텍스트
    private String companySupportAmountGuide = "";
    private int colonoscopyLimitAge = 0;                // 대장내시경제한나이
    private String familySupportType = "";              // 가족지원타입 10:개별 20:직원제외 30:직원포함 90:사용안함
    private String companySupportRange = "";            // 가족지원범위 10:배우자 20:자녀 30:부 31:배우자부 40:모 41:배우자모 50:기타
    private String familySupportAmountGuide = "";       // 가족지원금액안내
    private String overallCheckupYn = "";               // 종합검진여부
    private String nhisCheckupYn = "";                  // 공단검진여부
    private String specialCheckupYn = "";               // 특수검진여부
    private long expectSalesAmount = 0L;
    private String expectSalesAmountInvoiceBillYn = "";
    private String compcardBillYn = "";
    private String payrollBillYn = "";
    private String invoiceBillYn = "";
    private String changeAdminId = "";                  // 변경사용자아이디
    private String changeDt = "";                       // 변경일시
    private String companySupportYn = "";
    private String migYn = "";
    private String migDt = "";
    private String chargeType = "";                     // 청구타입 10:회사 20:개인
    private String companySupportAmountVisibility = ""; // 기업 지원금 표기 여부 10:표기, 90:미표기
    private String contractType = "";                   // 계약 형태 10:WITHKIM 20: 제3자계약
    private String fromCustomerId = "";                 // 복사 : 원본 정책의 고객사 아이디
    private String fromPolicyYear = "";                 // 복사 : 원본 정책의 정책연도
    private String productOrderType = "";               // 사용자 화면 상품 표시 기준 10:가나다순, 20:금액(적은 순), 30:금액(많은 순)
    private String healthcareCd = "";                   // 헬스케어 코드(어떠케어 : C000002, 현대이지웰 : C000003, 선헬스케어 : C000004, 케어링크 : C000005, 인피니티케어 : C000012)
    private String surveyProgressSt = "";
    private String surveyType = "";                     // 만족도 조사 유형 설정[10 : 검진기관 만족도 조사, 20 : 검진기관 만족도 조사 + 플랫폼 만족도 조사, 30 : 사용한함]
}