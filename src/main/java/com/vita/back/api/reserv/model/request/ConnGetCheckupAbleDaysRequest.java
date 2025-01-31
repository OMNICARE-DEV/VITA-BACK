package com.vita.back.api.reserv.model.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ConnGetCheckupAbleDaysRequest {
	private String partnerCenterId = ""; //협력병원아이디
    private String startDt = "";
    private String endDt = "";
    private String partnerCenterIdByCenter = ""; //협력병원측협력병원아이디
    private String specialCheckupYN = ""; //특수검진여부
    private String equipRoomIdByCenter = "";
    private List<String> centerPlaceIdList = new ArrayList<>();
    private List<String> centerPlaceIdByCenterList = new ArrayList<>();
    private List<String> equipRoomIdByCenterList = new ArrayList<>();
}