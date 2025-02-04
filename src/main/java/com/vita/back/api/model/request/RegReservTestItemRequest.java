package com.vita.back.api.model.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RegReservTestItemRequest {
	private String partnerCenterId = "";
    private int reservNo = 0;
    private int deferReservNo = 0;
    private List<CheckupItemList> checkupItemList = new ArrayList<>();
    private int CHECKUP_PRODUCT_NO = 0;
    
    @Data
    public static class CheckupItemList {
        private String itemDivCd = ""; //10:기본,20:선택,30:추가
        private String checkupItemCd = "";
        private int choiceGroupNo = 0;
        private int choiceBundleNo = 0;
        private int selfPayAmount = 0;
        private int onpayAmount = 0;
        private String deferCheckupYN ="";
        private String testItemCd = "";
        private String replaceItem = "";
        private String hoidYn = "";
        private String nonPriceYN = "N";
        private String customPriceYN = "N";
        private int customTotalPrice = 0;
        private int customCompanyPrice = 0;
        private int customSelfPrice = 0;
        private String replaceItemYn = "";
        private String pkgCd;
        private String addCheckupYN = "N";
        private int checkupRosterNo;
        private int reservMomentAmount = 0;
    }
}