package com.vita.back.api.reserv.model.data;

import lombok.Data;

@Data
public class CheckupPolicyDto {
	private String customerId;                        // 고객사아이디
    private String policyYear;                        // 정책연도
    private Integer personnel;                        // 인원
    private Integer familySupportAmount;              // 가족지원금액 (삭제예정)
    private String settleCycleCd;                     // 정산주기코드
    private String checkupDeferPermitYn;              // 검진연기허용여부
    private String productDisplayType;                // 상품표시타입
    private String reservStartDt;                     // 예약시작일시
    private String reservEndDt;                       // 예약종료일시
    private String checkupStartDt;                    // 검진시작일시
    private String checkupEndDt;                      // 검진종료일시
    private String familyCheckupStartDt;              // 가족검진시작일시
    private String familyCheckupEndDt;                // 가족검진종료일시
    private String checkupDivCd;                      // 검진구분코드
    private String colonoscopyLimitYn;                // 대장내시경제한여부
    private String visitCheckupYn;                    // 출장검진여부
    private String employCheckupYn;                   // 채용검진여부
    private String vaccineText;                       // 백신텍스트
    private String workenvReportUrl;                  // 작업환경측정서URL
    private String specialCheckupReqmUrl;             // 특수검진요청서URL
    private String specialCheckupAdminId;             // 특수검진운영자아이디
    private String nhisResultAllYn;                   // 공단결과전체여부
    private String billCycleCd;                       // 청구주기코드
    private String invoiceIssueCd;                    // 계산서발행코드
    private String employRecheckupSupportType;        // 채용재검진지원타입
    private String nhisMinusYn;                       // 공단차감여부
    private String billTargetCd;                      // 청구대상코드
    private String billUniquenessGuide;               // 청구특이사항안내
    private byte[] checkupPolicyGuide;                // 검진정책안내
    private String note;                              // 비고
    private String regDt;                             // 등록일시
    private Integer companySupportAmount;             // 회사지원금액
    private String etcCheckupText;                    // 기타검진텍스트
    private String companySupportAmountGuide;         // 회사지원금액안내
    private Integer colonoscopyLimitAge;              // 대장내시경제한나이
    private String familySupportType;                 // 가족지원타입
    private String companySupportRange;               // 가족지원범위
    private String familySupportAmountGuide;          // 가족지원금액안내
    private String overallCheckupYn;                  // 종합검진여부
    private String nhisCheckupYn;                     // 공단검진여부
    private String specialCheckupYn;                  // 특수검진여부
    private Long expectSalesAmount;                   // 예상매출금액
    private String expectSalesAmountInvoiceBillYn;    // 예상매출금액 청구 여부
    private String compcardBillYn;                    // 복지카드 청구 여부
    private String payrollBillYn;                     // 급여 청구 여부
    private String invoiceBillYn;                     // 계산서 청구 여부
    private String changeAdminId;                     // 변경사용자아이디
    private String changeDt;                          // 변경일시
    private String companySupportYn;                  // 회사지원여부
    private String migYn;                             // 마이그레이션 여부
    private String migDt;                             // 마이그레이션 날짜
    private String chargeType;                        // 청구타입
    private String companySupportAmountVisibility;    // 기업 지원금 표기 여부
    private String contractType;                      // 계약 형태
    private String fromCustomerId;                    // 원본 정책 고객사 아이디
    private String fromPolicyYear;                    // 원본 정책의 정책연도
    private String productOrderType;                  // 사용자 화면 상품 표시 기준
    private String healthcareCd;                      // 헬스케어 코드
    private String surveyProgressSt;                  // 설문 진행 상태
    private String surveyType;                        // 설문 유형
}