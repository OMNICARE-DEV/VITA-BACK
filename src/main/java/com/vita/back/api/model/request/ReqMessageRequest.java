package com.vita.back.api.model.request;

import lombok.Data;

@Data
public class ReqMessageRequest {
	private String message = ""; // not null
    private String templateCode = ""; // not null
    private String receivePhoneNo = ""; // not null
    private String buttonTitle = ""; // 버튼형일 경우 사용. (기본형/버튼형)
    /*
    기본형, 버튼형 모두 Url 이동 가능
     */
    private String mobileUrl = "";
    private String pcUrl = "";
    private String customerId = "";
    private String name = "";
    /** 배치여부 */
    private String batchYn;
    /* 예약번호 */
    private String reservNo;
    /* 해싱된 예약번호 **/
    private String hashedReservNo;
}