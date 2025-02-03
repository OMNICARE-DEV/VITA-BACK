package com.vita.back.api.reserv.model.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ReservMemoDto {
	private int reservMemoNo;       // 예약메모번호
    private int checkupRosterNo;    // 검진대상자번호
    private int reservNo;           // 예약번호
    private String regAdminId;      // 등록운영자아이디
    private String regAdminName;    // 등록운영자이름
    private String memoTitle;       // 메모제목
    private String memoContents;    // 메모내용
    private String regDt;           // 등록일시 (yyyyMMddHHmmss)
    private String migYn;           // 마이그레이션 여부
    private String migDt;           // 마이그레이션 일시 (yyyyMMdd)
}