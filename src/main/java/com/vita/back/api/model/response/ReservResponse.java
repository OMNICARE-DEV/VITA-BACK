package com.vita.back.api.model.response;

import java.util.ArrayList;
import java.util.List;

import com.vita.back.api.model.request.ConnDeferRegReservRequest;

import lombok.Data;

@Data
public class ReservResponse {
	private int reservNo;
    private int deferCheckupReservNo;
    private String reservSt = "";
    private String reservDay = "";
    private String deferReservDay = "";
    private String openPayPopupYn = "N";
    private String smspayYn = "N";
    private String partnerCenterId = "";
    private String reservIdByCenter = "";
    private List<ConnDeferRegReservRequest> connDeferRegReservReqList = new ArrayList<>();
    private List<RegReservTestItemResponse> regReservTestItemResponseList = new ArrayList<>();
    private String customerId = "";
    private int selfPayAmount = 0;
    private int checkupRosterNo = 0;
}
