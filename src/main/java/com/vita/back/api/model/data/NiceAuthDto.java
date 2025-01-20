package com.vita.back.api.model.data;

import lombok.Data;

@Data
public class NiceAuthDto {
    /* 공통 */
    /** 결과코드 */
    private String resultcode;
    /** 요청 고유 번호(회원사에서 전달보낸 값) */
    private String requestno;
    /** 암호화 일시(YYYYMMDDHH24MISS) */
    private String enctime;
    /** 사이트코드 */
    private String sitecode;
    /** 이름 */
    private String name;
    /** UTF8로 URLEncoding된 이름 값 */
    private String utf8_name;
    /** 내외국인 정보 */
    private String nationalinfo;
    /** 생년월일 8자리 (YYYYMMDD) */
    private String birthdate;
    /** 요청 시 전달 받은 RECEIVEDATA */
    private String receivedata;
    
    /* 휴대폰 */
    /** 응답 고유 번호 */
    private String responseno;
    /** 인증수단 */
    private String authtype;
    /** 성별 */
    private String gender;
    /** 이동통신사 구분 (휴대폰 인증 시) */
    private String mobileco;
    /** 휴대폰 번호 (휴대폰 인증 시) */
    private String mobileno;
    /** 개인 식별 코드(CI) */
    private String ci;
    /** 개인 식별 코드(DI) */
    private String di;
    /** 사업자 번호 (법인 인증서 인증 시) */
    private String businessno;
    
    /* IPIN */
    /** IP 주소 */
    private String ipaddr;
    /** 인증 번호 */
    private String vnumber;
    /** 성별 코드 */
    private String gendercode;
    /** 연령대 코드 */
    private String agecode;
    /** 중복 정보 (DI) */
    private String dupinfo;
    /** CI 정보 1 */
    private String coinfo1;
    /** CI 정보 2 */
    private String coinfo2;
    /** CI 갱신 여부 */
    private String ciupdate;
    /** 인증 방법 */
    private String authmethod;
    
    /** 리턴 URL */
    private String returnurl;
    /** 팝업 여부 */
    private String popupyn;
    /** 요청 방법 타입 */
    private String methodtype;
}
