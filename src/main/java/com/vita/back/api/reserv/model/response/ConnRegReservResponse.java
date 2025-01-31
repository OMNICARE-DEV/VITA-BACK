package com.vita.back.api.reserv.model.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ConnRegReservResponse {
	private Integer reservNo;
    private String reservIdByCenter = "";
    private String reservSt = "";
    private String reservDay = "";
    private String deferReservIdByCenter = "";
    private String deferReservSt = "";
    private List<String> holdTestItemList = new ArrayList<>();
    private String PremiumCapaYN = "N";
}