package com.hops.hops_new_api.common.model.data;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class NiceAuthDto {
    // 공통
    private String resultcode = ""; //결과코드
    private String requestno = ""; //요청 고유 번호(회원사에서 전달보낸 값)
    private String enctime = ""; //암호화 일시(YYYYMMDDHH24MISS)
    private String sitecode = ""; //사이트코드
    private String name = ""; //이름
    private String utf8_name = ""; //UTF8로 URLEncoding된 이름 값
    private String nationalinfo = ""; //내외국인
    private String birthdate = ""; //생년월일 8자리
    private String receivedata = ""; //요청 시 전달 받은 RECEIVEDATA

    // mobiel auth
    private String responseno = ""; //응답고유번호
    private String authtype = ""; //인증수단
    private String gender = ""; //성별
    //    private String mobile_co = ""; //이통사 구분(휴대폰 인증 시)
    //    private String mobile_no = ""; //휴대폰 번호(휴대폰 인증 시)
    private String mobileco = ""; //이통사 구분(휴대폰 인증 시)
    private String mobileno = ""; //휴대폰 번호(휴대폰 인증 시)
    private String ci = ""; //개인 식별 코드(CI)
    private String di = ""; //개인 식별 코드(DI)
    private String businessno = ""; //사업자번호(법인인증서 인증시)

    // ipin auth
    private String ipaddr = "";
    private String vnumber = "";
    private String gendercode = "";
    private String agecode = "";
    private String dupinfo = ""; // DI
    private String coinfo1 = ""; // CI
    private String coinfo2 = ""; // CI2
    private String ciupdate = ""; // CI2
    private String authmethod = ""; // CI2

    private String returnurl = "";
    private String popupyn = "";
    private String methodtype = "";

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }
}
