package com.vita.back.api.model.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ConnGetCheckupAbleDaysResponse {
	private List<String> reservAbleDayList = new ArrayList<>();
    private List<String> reservAbleEquipDayList = new ArrayList<>();
    private List<String> reservDisableDayList = new ArrayList<>();
}