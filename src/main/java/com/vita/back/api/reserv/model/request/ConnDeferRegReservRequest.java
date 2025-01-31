package com.vita.back.api.reserv.model.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ConnDeferRegReservRequest {
	private String partnerCenterIdByCenter = "";
    private String reservIdByCenter = "";
    private List<String> deferItemList = new ArrayList<>();
    private String deferPartnerCenterIdByCenter = "";
    private String derferReservDt = "";
    private String deferReservDt = "";
    private String deferReservIdByCenter = "";
    private String userId = "";
    private String target = "";
    private int reservNo = 0;
    private int checkupProductNo = 0;
    private String partnerCenterId = "";
    private String checkupItemCd = "";
    private String deleteDeferReservIdyByCenter = "";
    private int choiceGroupNo = 0;
    private int choiceBundleNo = 0;
    private List<KicsDeferItem> kicsDeferItemList = new ArrayList<>();
    private String deferReservTime = "";
    private String deferReservFloor = "";
    private String deferReservMemo="";
    private String smsYN = "";

    @Data
    public static class KicsDeferItem {
        private String pkgCd = "";
        private String testItemCd = "";
        private String groupCd = "";
    }
}