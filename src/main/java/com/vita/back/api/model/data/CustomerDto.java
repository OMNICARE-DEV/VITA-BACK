package com.vita.back.api.model.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CustomerDto {
	/** 고객사 아이디 */
    @Schema(description = "고객사 아이디")
    private String customerId;

    /** 고객사 이름 */
    @Schema(description = "고객사 이름")
    private String customerName;

    /** 대표이사 이름 */
    @Schema(description = "대표이사 이름")
    private String ceoName;

    /** 사업자 등록번호 */
    @Schema(description = "사업자 등록번호")
    private String corpNo;

    /** 주소 */
    @Schema(description = "주소")
    private String address;

    /** 상세 주소 */
    @Schema(description = "상세 주소")
    private String addressDetail;

    /** 우편번호 */
    @Schema(description = "우편번호")
    private String zipCd;

    /** 대표 고객사 아이디 */
    @Schema(description = "대표 고객사 아이디")
    private String mainCustomerId;

    /** 영업 운영자 아이디 */
    @Schema(description = "영업 운영자 아이디")
    private String salesAdminId;

    /** 운영자 아이디 */
    @Schema(description = "운영자 아이디")
    private String adminId;

    /** 회사 로고 URL */
    @Schema(description = "회사 로고 URL")
    private String companyLogoUrl;

    /** 고객사 도메인 */
    @Schema(description = "고객사 도메인")
    private String customerDomain;

    /** 고객사 도메인 사용 여부 */
    @Schema(description = "고객사 도메인 사용 여부 (Y/N)")
    private String customerDomainYn;

    /** 로그인 타입 (10: 공동, 20: 고객사 페이지) */
    @Schema(description = "로그인 타입 (10: 공동, 20: 고객사 페이지)")
    private String loginType;

    /** 연락처 등록 여부 */
    @Schema(description = "연락처 등록 여부 (Y/N)")
    private String mobileNoYn;

    /** 등록 일시 */
    @Schema(description = "등록 일시 (YYYYMMDDHHMMSS)")
    private String regDt;

    /** 고객사 상태 (00: 정상, 90: 삭제) */
    @Schema(description = "고객사 상태 (00: 정상, 90: 삭제)")
    private String customerSt;

    /** 대상자 아이디 타입 (10: 사번, 20: 통합아이디) */
    @Schema(description = "대상자 아이디 타입 (10: 사번, 20: 통합아이디)")
    private String rosterIdType;

    /** 고객사 노출 여부 */
    @Schema(description = "고객사 노출 여부 (Y/N)")
    private String customerDisplayYn;

    /** 마이그레이션 여부 */
    @Schema(description = "마이그레이션 여부 (Y/N)")
    private String migYn;

    /** 마이그레이션 일시 */
    @Schema(description = "마이그레이션 일시 (YYYYMMDD)")
    private String migDt;

    /** 명단 추가 타입 */
    @Schema(description = "명단 추가 타입")
    private String rosterAddType;

    /** 파트너 헬스케어 ID */
    @Schema(description = "파트너 헬스케어 ID")
    private String partnerHcId;

    /** 회사 검진 정책 가이드 */
    @Schema(description = "회사 검진 정책 가이드")
    private String companyCheckupPolicyGuide;

    /** 회사 검진 PIL */
    @Schema(description = "회사 검진 PIL")
    private String companyCheckupPil;

    /** 청구 유일성 내용 */
    @Schema(description = "청구 유일성 내용")
    private String billUniquenessContents;

    /** 로그인 URL */
    @Schema(description = "로그인 URL")
    private String loginUrl;

    /** 로그인 설정 타입 */
    @Schema(description = "로그인 설정 타입")
    private String loginConfType;

    /** 비고 (KICS 사업장 상세 관리) */
    @Schema(description = "비고 (KICS 사업장 상세 관리)")
    private String note;

    /** 최소 대기일 */
    @Schema(description = "최소 대기일")
    private String minWaitingDay;

    /** 최소 대기일 사용 여부 (Y/N) */
    @Schema(description = "최소 대기일 사용 여부 (Y/N)")
    private String minWaitingDayYn;

    /** 업태 */
    @Schema(description = "업태")
    private String businessType;

    /** 종목 */
    @Schema(description = "종목")
    private String businessItem;

    /** 추가 테스트 여부 */
    @Schema(description = "추가 테스트 여부 (Y/N)")
    private String addTestYn;

    /** 고객사 인증 코드 */
    @Schema(description = "고객사 인증 코드")
    private String customerCertifyCd;

    /** 고객사 인증 코드 타입 */
    @Schema(description = "고객사 인증 코드 타입")
    private String customerCertifyCdType;

    /** 제휴병원 우선 표시 여부 (Y: 설정, N: 미설정) */
    @Schema(description = "제휴병원 우선 표시 여부 (Y: 설정, N: 미설정)")
    private String corporateDisplayYn;
}
