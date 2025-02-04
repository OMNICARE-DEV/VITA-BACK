package com.vita.back.api.model.request;

import lombok.Data;

@Data
public class RosterResponse {
	private String customerId = "";
    private int checkupRosterNo = 0;
    private int superCheckupRosterNo = 0;
    private String rosterName = "";
    private String birthday = "";
    private String mobileNo = "";
}