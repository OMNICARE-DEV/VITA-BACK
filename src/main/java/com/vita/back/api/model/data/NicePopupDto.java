package com.vita.back.api.model.data;

import lombok.Data;

@Data
public class NicePopupDto {
    /** 서비스 요청 고유 번호 */
    private String requestno;
    /** 인증결과를 받을 회원사 URL */
    private String returnurl;
    /** 암호화토큰요청 API에 응답받은 site_code */
    private String sitecode;
    /** 인증수단 고정 (M:휴대폰인증, C:카드본인확인인증, X:인증서인증, U:공동인증서인증, F:금융인증서인증, S:PASS인증서인증) */
    private String authtype;
    /** 이통사 우선 선택(S: SKT, K: KT, L: LGU+) */
    private String mobilceco;
    /** 사업자번호(법인인증에 한함) */
    private String businessno;
    /** 결과 URL 전달 시 HTTP 메서드 타입 */
    private String methodtype;
    /** 팝업 여부 */
    private String popupyn;
    /** 인증 후 전달받을 데이터 세팅 (요청값 그대로 리턴) */
    private String receivedata;
}
