package com.vita.back.api.reserv.model.data;

import lombok.Data;

@Data
public class UpdtCompanySupAmtDto {
	public int rosterNo;
    public String employRelationType = "";  //임직원 관계
    public String familySupportType = "";   //가족회사지원타입
    public String mgmtType = "";
    public int companySupportAmt;   //회사지원금
    public int companySupportUsageAmt;   //(본인)사용한 가족회사지원금
    public int companySupportChargeAmt;   //(본인)사용한 가족회사지원금
    public int superRosterCompanySupportUsageAmt;  // (임직원)사용한 가족회사지원금
    public int superRosterNo;
    public int superRosterCompanySupportChargeAmt;
    public String regedPath;
}