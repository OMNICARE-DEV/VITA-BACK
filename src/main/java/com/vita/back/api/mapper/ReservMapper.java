package com.vita.back.api.mapper;

import com.vita.back.api.model.data.*;
import com.vita.back.api.model.request.AgreeTermsRequest;
import com.vita.back.api.model.request.CheckupRosterRequest;
import com.vita.back.api.model.request.ReservRequest;
import com.vita.back.api.model.request.UpdateRosterRequest;
import com.vita.back.api.model.response.UpdtReservConnResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReservMapper {
	CheckupRosterDto selectCheckupRoster(CheckupRosterRequest hcCheckupRosterRequest);

	CenterConnecterInfoDto selectHcCenterConnecter(CenterConnecterInfoDto hcCenterConnecterInfoVo);

	ExtIntegrationDto selectExtConnection(String partnerCenterId);

	CheckupProductDto selectCheckupProduct(CheckupProductDto checkupProductVo);

	CheckupPlaceDto selectCheckupPlace(CheckupPlaceDto checkupPlaceVo);

	UpdtCompanySupAmtDto selectCompanySup(int rosterNo);

	void insertReserv(RegReservDto regReservVo);

	//String selectCustomerId(RegReservVo regReservVo);

	//String selectMobileNoYn(String customerId);

	//String selectCheckupRosterMobileNo(RegReservVo regReservVo);

	void updateCheckupRosterMobileNo(RegReservDto regReservVo);

	List<String> selectDeferCheckupItem();

	String selectTestItemCd(String string, String partnerCenterId);

	void insertDeferValIdCheckupTestItem(RegDeferReservValIdDto regDeferReservVal);

	void insertDefferReserv(DeferCheckupReservDto deferReservVo);

	void insertDeferReservTestItem(DeferReservTestItemDto deferReservTestItemVo);

	void updateDeferValidCheckupTestItem(DeferReservTestItemDto deferReservTestItemVo);

	List<GetTestItemCdDto> selectTestItemCd(GetTestItemCdDto getTestItemCdVo);

	List<ReservTestItemDto> selectMappingCd(GetTestItemCdDto getTestItemCdVo);

	void insertReservTestItem(ReservTestItemDto reservTestItemVo);

	String selectGroupCapaNo(Map<String, String> map);

	List<String> selectTestItemCdStringList(int reservNo);

	String selectSpecialRoster(int rosterNo);

	void updatePlusSpecialCapa(GetRemainCapaDto capaVo);

	void updateMinusSpecialCapa(GetRemainCapaDto capaVo);

	List<Integer> selectEquipNo(GetEquipNoDto getEquipNoVo);

	void updatePlusCapa(GetRemainCapaDto capaVo);

	void updateMinusCapa(GetRemainCapaDto capaVo);

	void updatePlusEquipCapa(GetRemainCapaDto capaVo);

	void updateMinusEquipCapa(GetRemainCapaDto capaVo);

	void updatePlusGroupCapa(Map<String, String> map);

	void updateMinusGroupCapa(Map<String, String> map);

	void updatePlusGroupEquipCapa(Map<String, String> map);

	void updateMinusGroupEquipCapa(Map<String, String> map);

	String selectContractType(Map<String, Object> map);

	List<ReservTestItemDto> selectTestItemCdList(RegReservDto regReservVo);

	// String selectCustomerIdByRoster(int rosterNo);

	ServiceInfoDto selectHcServiceInfo(String serviceInfoId);

	CheckupSuperRosterDto selectSuperRosterInfo(int checkupRosterNo);

	void updateReservTestItem(int reservNo, List<String> holdTestItemList);

	String selectTestItem(String partnerCenterId, String checkupItemCd);

	int updateMinusPremiumCapa(String valueOf, String reserv1stHopeTime, String partnerCenterId);

	void updateRestorePremiumCapaYN(ReservRequest request);

	String selectRoomInfo(GetRoomInfoDto roomVo);

	List<String> selectBundleRoomInfo(String testItemCd);

	List<String> selectCheckupHolidayInfo();

	String selectServiceCustomerId(CenterCustomerInfoDto hcCenterCustomerInfoVo);

	//Object selectUnRegEmployYn(int checkupRosterNo);

	CheckupSuperRosterDto selectUnRegEmployInfo(int checkupRosterNo);

	void updateReservPremiumCapa(String premiumCapaYN, int reservNo);

	boolean selectPremiumCapaProduct(int checkupProductNo);

	void updateReservSt(CommonUpdtReservStDto vo);

	void updateDefferReservSt(CommonUpdtReservStDto vo);

	void updateRoster(UpdateRosterRequest updateRosterRequest);

	void updateCompanySupportUsageAmount(UpdtCompanySupAmtDto vo);

	void updateReservConnResp(UpdtReservConnResponse updtReservConnRespVo);

	void updateDeferReservConnResp(UpdtReservConnResponse updtReservConnRespVo);

	void updateSpecialYn(int checkupRosterNo);

	String selectAgreeTerms(HashMap<String, Object> map);

	HashMap<String, Object> selectRosterInfo(int checkupRosterNo);

	void insertAgreeTerms(AgreeTermsRequest dto);

	void updatePersonalPayerBilling(Map<String, Object> param);

	int selectEKGItem(int reservNo);

	void updateEKGItem(int reservNo);

	int selectDivCd(int reservNo);

	String selectRosterBirthday(int reservNo);

	int selectEchoItem(int reservNo);

	int selectSedationItem(int reservNo);

	CheckupPolicyDto selectCheckupPolicy(CheckupPolicyDto checkupPolicyDto);

	PartnerCenterDto selectPartnerCenter(PartnerCenterDto partnerCenterDto);

	List<RegReservDto> selectReservInfo(RegReservDto reservDto);

	ReservMemoDto selectReservMemo(int checkupRosterNo);

	//HcCheckupRosterResponse selectCheckupRosterDetails(HcCheckupRosterRequest hcCheckupRosterRequest);

}
