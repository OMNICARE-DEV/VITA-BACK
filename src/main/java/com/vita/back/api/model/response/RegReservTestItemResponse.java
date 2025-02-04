package com.vita.back.api.model.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RegReservTestItemResponse {
	List<TestItemList> testItemInfoList = new ArrayList<>();
	
	@Data
    public static class TestItemList {
        private String itemDivCd = "";
        private String testItemCd = "";
        private String checkupItemCd = "";
        private int choiceGroupNo = 0;
        private int choiceBundleNo = 0;
        private int addTestPrice = 0;
        private String deferCheckupYN = "";
        private String holdYn="";
        private String nonPriceYN = "";
        private String customPriceYN = "";
        private int customTotalPrice;
        private int customCompanyPrice;
        private int customSelfPrice;
        private int onpayAmount;
        private int selfPayAmount;
        private String pkgCd;
    }
}