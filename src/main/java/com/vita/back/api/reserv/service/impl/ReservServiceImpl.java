package com.vita.back.api.reserv.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.vita.back.api.mapper.UserLoginMapper;
import com.vita.back.api.model.data.CenterConnecterInfoDto;
import com.vita.back.api.model.data.CommonUserDto;
import com.vita.back.api.model.data.CustomerMapDto;
import com.vita.back.api.model.data.ExtIntegrationDto;
import com.vita.back.api.model.data.UserCertifyDto;
import com.vita.back.api.model.data.UserDto;
import com.vita.back.api.model.data.UserIdDupCheckDto;
import com.vita.back.api.model.request.RegCommonUserRequest;
import com.vita.back.api.model.request.UserLoginRequest;
import com.vita.back.api.model.response.RegCommonUserResponse;
import com.vita.back.api.model.response.UserLoginResponse;
import com.vita.back.api.pay.model.UpdatePayReservNoRequest;
import com.vita.back.api.pay.service.PayService;
import com.vita.back.api.reserv.mapper.ReservMapper;
import com.vita.back.api.reserv.model.data.AgreeTermsDto;
import com.vita.back.api.reserv.model.data.CenterCustomerInfoDto;
import com.vita.back.api.reserv.model.data.CheckupPlaceDto;
import com.vita.back.api.reserv.model.data.CheckupProductDto;
import com.vita.back.api.reserv.model.data.CheckupRosterDto;
import com.vita.back.api.reserv.model.data.CheckupSuperRosterDto;
import com.vita.back.api.reserv.model.data.CommonUpdtReservStDto;
import com.vita.back.api.reserv.model.data.DeferCheckupReservDto;
import com.vita.back.api.reserv.model.data.DeferReservTestItemDto;
import com.vita.back.api.reserv.model.data.EmployRelationType;
import com.vita.back.api.reserv.model.data.GetEquipNoDto;
import com.vita.back.api.reserv.model.data.GetRemainCapaDto;
import com.vita.back.api.reserv.model.data.GetRoomInfoDto;
import com.vita.back.api.reserv.model.data.GetTestItemCdDto;
import com.vita.back.api.reserv.model.data.RegDeferReservValIdDto;
import com.vita.back.api.reserv.model.data.RegReservDto;
import com.vita.back.api.reserv.model.data.ReservTestItemDto;
import com.vita.back.api.reserv.model.data.ReserveDataDto;
import com.vita.back.api.reserv.model.data.ServiceInfoDto;
import com.vita.back.api.reserv.model.data.UpdtCompanySupAmtDto;
import com.vita.back.api.reserv.model.request.AgreeTermsRequest;
import com.vita.back.api.reserv.model.request.CheckupRosterRequest;
import com.vita.back.api.reserv.model.request.ConnGetCheckupAbleDaysRequest;
import com.vita.back.api.reserv.model.request.ConnRegReservRequest;
import com.vita.back.api.reserv.model.request.RegReservTestItemRequest;
import com.vita.back.api.reserv.model.request.RegSpecialReservRequest;
import com.vita.back.api.reserv.model.request.ReqMessageRequest;
import com.vita.back.api.reserv.model.request.ReservRequest;
import com.vita.back.api.reserv.model.request.RosterRequest;
import com.vita.back.api.reserv.model.request.RosterResponse;
import com.vita.back.api.reserv.model.request.UpdateRosterRequest;
import com.vita.back.api.reserv.model.response.ConnGetCheckupAbleDaysResponse;
import com.vita.back.api.reserv.model.response.ConnRegReservResponse;
import com.vita.back.api.reserv.model.response.RegReservTestItemResponse;
import com.vita.back.api.reserv.model.response.ReqMessageResponse;
import com.vita.back.api.reserv.model.response.ReservResponse;
import com.vita.back.api.reserv.model.response.UpdtReservConnResponse;
import com.vita.back.api.reserv.service.ReservService;
import com.vita.back.api.u2bio.model.data.U2bioPatientDto;
import com.vita.back.api.u2bio.model.request.U2bioReservAvailableDateRequest;
import com.vita.back.api.u2bio.service.U2BioService;
import com.vita.back.api.u2bio.service.impl.U2BioServiceImpl;
import com.vita.back.common.exception.VitaCode;
import com.vita.back.common.exception.VitaException;
import com.vita.back.common.util.Connection;
import com.vita.back.common.util.ValidUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
//noRollbackFor = { HopsNoRollbackException.class } -> 추후 정의
@Transactional(rollbackFor = { Exception.class, VitaException.class })
public class ReservServiceImpl implements ReservService {
	private final ReservMapper mapper;
	private final U2BioService u2bioService;
	private final PayService payService;
	private final Connection connection;

	public ReservServiceImpl(ReservMapper mapper, U2BioServiceImpl u2BioService, PayService payService,
			Connection connection) {
		this.mapper = mapper;
		this.u2bioService = u2BioService;
		this.payService = payService;
		this.connection = connection;
	}

	private final String MSG_RESERV_REG = "2112"; // 예약 신청
	private final String MSG_RESERV_DONE = "2113"; // 예약 확정

	private final String SLEEP_COLON = "T00223"; // 대장(수면)
	private final String NORMAL_COLON = "T00222"; // 대장(일반)
	private final String SLEEP_GASTRO = "T00221"; // 위(수면)
	private final String NORMAL_GASTRO = "T00220"; // 위(일반)
	private final String NORMAL_COLON_TEST_ITEM_CD = "KMI015"; // 대장(일반)
	private final String SLEEP_COLON_TEST_ITEM_CD = "KMI014"; // 대장(수면)

	private int globalOriginAmount = 0;
	private int globalOriginRosterNo = 0;
	private int globalFinalReservCompanySupportAmount = 0;

	@Override
	public ReservResponse processReserv(ReservRequest request) throws VitaException {

		// 누락된 주소 업데이트
		updateMissingAddresses(request);

		// 필요한 정보 조회
		CheckupRosterDto checkupRosterDto = fetchCheckupRosterInfo(request);
		CheckupProductDto checkupProductDto = fetchCheckupProductInfo(request);
		CenterConnecterInfoDto centerConnecterInfoDto = fetchCenterConnecterInfo(request);
		ExtIntegrationDto extIntegrationDto = fetchExternalIntegrationInfo(request);

		RegReservDto regReservVo = makeRegReservVo(request, checkupRosterDto, checkupProductDto);
		request.setCheckupPlaceId(regReservVo.getCheckupPlaceId());

		// 상품없을 경우 익셉션
		if (ValidUtil.isEmpty(checkupProductDto)) {
			throw new VitaException(VitaCode.PRODUCT_NOT_FOUND);
		}

		// 요청값 유효성 검사
		validateRequest(request, checkupRosterDto, checkupProductDto);

		/*
		 * 검증로직 추후 검토
		 * 
		 * // 대상자 검증 service.rosterValidationCheck(request, checkupRosterVo);
		 * 
		 * // 상품 검증 service.productValidationCheck(request, checkupProductVo);
		 * 
		 * // 정책검증 service.policyValidationCheck(request, checkupRosterVo);
		 * 
		 * // 병원검증 service.centerValidationCheck(request, checkupRosterVo);
		 * 
		 * // 예약검증 service.reservValidationCheck(request,
		 * checkupRosterVo.getCHECKUP_ROSTER_NO());
		 * 
		 * // 예약자 메모 정보조회
		 * request.setReservRequestContents(commonService.commonGetMemo(checkupRosterNo)
		 * );
		 */

		int reservNo = processReservation(regReservVo, request, checkupRosterDto, checkupProductDto, extIntegrationDto);
		int deferReservNo = processDeferredCheckup(request);

		request.setReservNo(reservNo);
		request.setDeferReservNo(deferReservNo);

		// 대상자의 휴대폰 번호 업데이트
		updateMobileNumber(checkupRosterDto, regReservVo);

		// 결제 정보 업데이트
		updatePaymentInfo(request, reservNo);

		// 예약 검진항목 등록
		regReservTestItem(request, new ArrayList<>());

		boolean isCenterNotConnected = ValidUtil.isEmpty(centerConnecterInfoDto);
		boolean isU2bioNotConnected = ValidUtil.isEmpty(extIntegrationDto);
		boolean isCenterProductNotIncluded = ValidUtil.isEmpty(checkupProductDto.getCenterProductNo());

		// 케파 갱신
		// --> 이부분 의문점 왜 협력병원 연계정보 없을 때만 케파갱신 처리 하는지??

		boolean isCenterNotKMI = isCenterNotConnected && (isU2bioNotConnected || isCenterProductNotIncluded);
		boolean isOtherCenterWithU2bio = !isU2bioNotConnected && !isCenterProductNotIncluded;
		if (isCenterNotKMI)
			updateCapacity(request);
		// regReservVo

		// Map<String,Object> map = new HashMap<>();
		// map.put("customerId",request.getCustomerId());
		// map.put("policyYear",request.getPolicyYear());

		// String contractType = mapper.getContractType(map);

		// PC/MOBILE에서 결제 완료 되어야 예약 등록으로 상태변경 처리(상태[대기]로 프로세스 종료)
//        if (request.getOnpayAmount() != 0 && contractType.equals("20") && request.getAdminReqYn().equals("Y")) {
//            // 결제리턴
//            RegReservResponse payResponse = new RegReservResponse();
//            String openPayPopupYn = "Y";
//            service.sendSmsPay(reservNo, request.getOnpayAmount(), checkupRosterVo.getROSTER_NAME(), request.getMobileNo(), checkupRosterVo.getMOBILE_NO());
//            payResponse.setSmspayYn("Y");
//            openPayPopupYn = "N";
//            payResponse.setReservNo(reservNo);
//            payResponse.setReservSt(regReservVo.getRESERV_ST());
//            payResponse.setOpenPayPopupYn(openPayPopupYn);
//
//            log.debug("for onpay Response: {}", payResponse);
//            return payResponse;
//        }
		/*
		 * ************************************** 신규예약 종료
		 * *****************************************
		 */

		/*******
		 * 기등록된 예약 데이터 상태 전환 (대기->취소 or 대기->등록[확정])
		 *********************************/
		// [구현부]
		/*******
		 * 기등록된 예약 데이터 상태 전환 (대기->취소 or 대기->등록[확정]) 종료
		 *********************************/

		// 예약검사항목 조회 2023-11-06
		List<ReservTestItemDto> testItemInfoList = mapper.selectTestItemCdList(regReservVo);
		log.info("예약번호:{}, 연기검진예약번호:{}", reservNo, deferReservNo);

		// 6-2. 병원 연계 진행

		String reservSt = "";
		String deferReservSt = "";
		String centerReservId = "";
		String deferReservIdByCenter = "";
		String confirmReservDay = "";
		String confirmDeferReservDay = "";

		if (isCenterNotKMI) { // 연계정보 없을 경우 Admin에서 확정처리
			// 이 경우 위에서 검증을 모두 거쳤기 때문에 할게없음.
			reservSt = "10"; // 예약신청
			if (!ValidUtil.isEmpty(request.getDeferReservHopeDay())) { // 연기검진 희망자라면
				deferReservSt = "10"; // 예약신청
			}
			sendKakaoMsg(MSG_RESERV_REG, regReservVo, "");
		} else if (isOtherCenterWithU2bio) { // 유투바이오

			sendReservationToU2Bio(regReservVo, extIntegrationDto, testItemInfoList);

			reservSt = "50"; // 예약확정
			confirmReservDay = request.getReserv1stHopeDay();

			sendKakaoMsg(MSG_RESERV_DONE, regReservVo, confirmReservDay);
		} else { // 연계정보 유효할 경우

			if ("10".equals(centerConnecterInfoDto.getConnecterType())) {

				ConnRegReservResponse connRegReservResponse = connToCenter(request, testItemInfoList, checkupRosterDto,
						regReservVo, centerConnecterInfoDto);

				confirmReservDay = connRegReservResponse.getReservDay();

				// 엑셀예약 선택미정, 검사보류 추가 40 : 예약보류 , 41 : 선택미정
				if (List.of("40", "41").contains(connRegReservResponse.getReservSt())) {
					reservSt = connRegReservResponse.getReservSt();
					deferReservSt = connRegReservResponse.getDeferReservSt();
					sendKakaoMsg(MSG_RESERV_REG, regReservVo, "");
				} else {
					reservSt = "50"; // 예약확정
					deferReservSt = connRegReservResponse.getDeferReservSt();
					sendKakaoMsg(MSG_RESERV_DONE, regReservVo, confirmReservDay);
				}

				/*
				 * 엑셀예약 선택미정, 검사보류 추가 원본 reservSt = "50"; // 예약확정 deferReservSt = "50"; // 예약확정.
				 * 검진과 연기검진의 예약상태는 함께 간다
				 */

				centerReservId = connRegReservResponse.getReservIdByCenter();
				confirmReservDay = connRegReservResponse.getReservDay();
				deferReservIdByCenter = connRegReservResponse.getDeferReservIdByCenter();
				confirmDeferReservDay = request.getDeferReservHopeDay(); // 성공했을 경우 connecter로부터 연기검진 일자가 반환되고 있지 않으므로
																			// request값을 넣는다.

				updateHoldItemStatus(reservNo, connRegReservResponse.getHoldTestItemList());
			}

			if ("70".equals(centerConnecterInfoDto.getConnecterType())) {
				reservSt = "50"; // 예약확정
				deferReservSt = "50"; // 예약확정
				confirmReservDay = request.getReserv1stHopeDay();
				confirmDeferReservDay = request.getDeferReservHopeDay();
			}

		}

		/***** 병원 연계 *******/
		/* 대상자 업데이트(VIP, 수송여부) */
		if (!request.getLastPath().equals("P") && !request.getLastPath().equals("M")) { // 명동콜센터에서만 제어 PC/MOBILE에선 제어 못함
			UpdateRosterRequest updateRosterRequest = new UpdateRosterRequest();
			updateRosterRequest.setOtYN(request.getTransCustomerYN());
			updateRosterRequest.setVipYN(request.getVipYN());
			updateRosterRequest.setCheckupRosterNo(request.getCheckupRosterNo());
			try {
				mapper.updateRoster(updateRosterRequest);
			} catch (Exception e) {
				log.warn("대상자 업데이트(VIP, 수송여부) 실패", e);
				throw new VitaException(VitaCode.DATABASE_ERROR);
			}
		}

		updateReservConnResp(reservSt, reservNo, centerReservId, confirmReservDay, request);

		if (!ValidUtil.isEmpty(request.getDeferReservHopeDay())) {
			updateDeferReservConnResp(reservNo, deferReservNo, deferReservIdByCenter, deferReservSt,
					confirmDeferReservDay, request);
		}

		if (!ValidUtil.isEmpty(checkupProductDto.getSpecialProductIdByCenter())) {
			addProductSpecialReserv(reservNo, request, checkupProductDto.getSpecialProductIdByCenter());
		}

		updateEKGItem(reservNo);

		insertAgreeTerms(regReservVo, request);

		/* 8. return response */
		ReservResponse response = new ReservResponse();
		response.setReservNo(reservNo);
		response.setReservSt(reservSt);
		response.setReservDay(confirmReservDay);
		response.setDeferReservDay(confirmDeferReservDay);

		return response;
	}

	/** 검진대상자 등록(없을 시) */
	public RosterResponse registRoster(ReservRequest request) throws VitaException {
		RosterRequest req = new RosterRequest();
		RosterResponse resp = new RosterResponse();
		req.setUserNo(request.getRegRosterRequest().getUserNo());
		req.setCustomerId(request.getRegRosterRequest().getCustomerId());
		req.setRosterName(request.getRegRosterRequest().getRosterName());
		req.setEmail(request.getRegRosterRequest().getEmail());
		req.setBirthday(request.getRegRosterRequest().getBirthday());
		req.setEmployNo(request.getRegRosterRequest().getEmployNo());
		req.setGenderCd(request.getRegRosterRequest().getGenderCd());
		req.setMobileNo(request.getRegRosterRequest().getMobileNo());
		req.setPhoneNo(request.getRegRosterRequest().getPhoneNo());
		req.setDepartment(request.getRegRosterRequest().getDepartment());
		req.setJobType(request.getRegRosterRequest().getJobType());
		req.setDomesticYn(request.getRegRosterRequest().getDomesticYn());
		req.setVipYn(request.getRegRosterRequest().getVipYn());
		req.setCheckupDivCd(request.getRegRosterRequest().getCheckupDivCd());
		req.setCompanySupportAmount(request.getRegRosterRequest().getCompanySupportAmount());
		req.setCompanySupporType(request.getRegRosterRequest().getCompanySupporType());
		req.setNhisTargetYn(request.getRegRosterRequest().getNhisTargetYn());
		req.setSpecialCheckupYn(request.getRegRosterRequest().getSpecialCheckupYn());
		req.setNighttimeTargetYn(request.getRegRosterRequest().getNighttimeTargetYn());
		req.setSpecialCheckupText(request.getRegRosterRequest().getSpecialCheckupText());
		req.setBeforeWorkTestYn(request.getRegRosterRequest().getBeforeWorkTestYn());
		req.setBeforeWorkTestText(request.getRegRosterRequest().getBeforeWorkTestText());
		req.setVisitCheckupTargetYn(request.getRegRosterRequest().getVisitCheckupTargetYn());
		req.setCheckupStartDt(request.getRegRosterRequest().getCheckupStartDt());
		req.setCheckupEndDt(request.getRegRosterRequest().getCheckupEndDt());
		req.setVaccineText(request.getRegRosterRequest().getVaccineText());
		req.setPrivacyAgreeYn(request.getRegRosterRequest().getPrivacyAgreeYn());
		req.setServiceAgreeYn(request.getRegRosterRequest().getServiceAgreeYn());
		req.setInfoShareAgreeYn(request.getRegRosterRequest().getInfoShareAgreeYn());
		req.setSensitiveInfoAgreeYn(request.getRegRosterRequest().getSensitiveInfoAgreeYn());
		req.setCheckupProposNo(request.getRegRosterRequest().getCheckupProposNo());
		req.setEmployRelationType(request.getRegRosterRequest().getEmployRelationType());
		req.setRelationEmployNo(request.getRegRosterRequest().getRelationEmployNo());
		req.setRelationRosterName(request.getRegRosterRequest().getRelationRosterName());
		req.setRelationBirthday(request.getRegRosterRequest().getRelationBirthday());
		req.setRelationMobileNo(request.getRegRosterRequest().getRelationMobileNo());
		req.setPolicyYear(request.getRegRosterRequest().getPolicyYear());
		req.setRegAdminId(request.getRegRosterRequest().getRegAdminId());
		req.setRosterMgmtType(request.getRegRosterRequest().getRosterMgmtType());
		req.setMemoContents(request.getRegRosterRequest().getMemoContents());

		req.setMerchantShipYn(request.getRegRosterRequest().getMerchantShipYn());
		req.setTransCustomerYN(request.getRegRosterRequest().getTransCustomerYN());

		// 임직원 미등록 대상자
		req.setUnRegEmployYN(request.getRegRosterRequest().getUnRegEmployYN());
		req.setRelationEmployGender(request.getRegRosterRequest().getRelationEmployGender());
		req.setRelationEmployEmail(request.getRegRosterRequest().getRelationEmployEmail());
		req.setRelationEmployJobtype(request.getRegRosterRequest().getRelationEmployJobtype());
		req.setRelationEmployPhone(request.getRegRosterRequest().getRelationEmployPhone());
		req.setRelationEmployDepartment(request.getRegRosterRequest().getRelationEmployDepartment());

		// 최초등록 경로
		req.setLastPath(request.getRegRosterRequest().getLastPath());

		// restTemplate
		try {
			resp = connection.request(req, RosterResponse.class, "customer/regroster", HttpMethod.POST);
		} catch (Exception e) {
			log.warn("연계실패 to (customer/regroster)", e);
			throw new VitaException(VitaCode.API_ERROR);
		}

		return resp;
	}

	/**
	 * 동의서 등록
	 * 
	 * @param regReservVo
	 * @param request
	 * @throws VitaException
	 */
	private void insertAgreeTerms(RegReservDto regReservVo, ReservRequest request) throws VitaException {
		/* 7. 동의서 등록처리 */
		AgreeTermsDto agreeTerms = new AgreeTermsDto();

		agreeTerms.setAgreePath(regReservVo.getLastPath());
		agreeTerms.setMobileNo(regReservVo.getMobileNo());
		agreeTerms.setCheckupRosterNo(regReservVo.getCheckupRosterNo());

		String[] agreeList = request.getUserTermsAgreeList().split(",");
		List<String> termsList = new ArrayList<>(Arrays.asList(agreeList));

		for (String terms : termsList) {
			switch (terms) {
			case "1":
				agreeTerms.setFirstConsent("Y");
				break;
			case "2":
				agreeTerms.setSecondConsent("Y");
				break;
			case "3":
				agreeTerms.setThirdConsent("Y");
				break;
			}
		}

		log.debug("동의서 등록: {}", agreeTerms);

		AgreeTermsRequest termsDTO = buildTermsDTO(agreeTerms);
		insertAgreeTerms(termsDTO);

		log.debug("동의서 등록 완료");
	}

	/**
	 * 특수검진 처리
	 * 
	 * @param reservNo
	 * @param request
	 * @param specialProductIdByCenter
	 * @throws VitaException
	 */
	private void addProductSpecialReserv(int reservNo, ReservRequest request, String specialProductIdByCenter)
			throws VitaException {
		/* 특수검진 동시 진행 */
		RegSpecialReservRequest regSpecialReservRequest = new RegSpecialReservRequest();
		regSpecialReservRequest.setReservNo(reservNo);
		regSpecialReservRequest.setLastPath(request.getLastPath());
		regSpecialReservRequest.setCenterProductId(specialProductIdByCenter);

		ReservResponse resp = connection.request(regSpecialReservRequest, ReservResponse.class,
				"/reserv/addproductspecialreserv", HttpMethod.POST);

		if (List.of("50", "40", "41").contains(resp.getReservSt())) {
			/* 특수검진 대상여부 업데이트 */
			mapper.updateSpecialYn(request.getCheckupRosterNo());
		}
	}

	/**
	 * 연기검진 예약 결과 처리(HC_DEFER_CHECKUP_RESERV 업데이트)
	 * 
	 * @param reservNo
	 * @param deferReservNo
	 * @param deferReservIdByCenter
	 * @param deferReservSt
	 * @param confirmDeferReservDay
	 * @param request
	 * @throws VitaException
	 */
	private void updateDeferReservConnResp(int reservNo, int deferReservNo, String deferReservIdByCenter,
			String deferReservSt, String confirmDeferReservDay, ReservRequest request) throws VitaException {
		UpdtReservConnResponse updtReservConnRespVo = new UpdtReservConnResponse();
		updtReservConnRespVo.setReservNo(reservNo);
		updtReservConnRespVo.setDeferCheckupReservNo(deferReservNo);
		updtReservConnRespVo.setCenterReservId(deferReservIdByCenter);
		updtReservConnRespVo.setReservSt(deferReservSt); // 예약확정
		updtReservConnRespVo.setReservDt(confirmDeferReservDay);
		updtReservConnRespVo.setLastPath(request.getLastPath());
		updtReservConnRespVo.setLastModifier(request.getLastModifier());

		try {
			mapper.updateDeferReservConnResp(updtReservConnRespVo);
		} catch (Exception e) {
			updateReservStatus("90", reservNo, request.getLastPath(), request.getLastModifier());
			log.warn("DB error (updtCenterReservId)", e);
			throw new VitaException(VitaCode.DATABASE_ERROR); // DB error
		}
	}

	/**
	 * 예약 결과 처리(HC_RESERV 업데이트)
	 * 
	 * @param reservSt
	 * @param reservNo
	 * @param centerReservId
	 * @param confirmReservDay
	 * @param request
	 * @throws VitaException
	 */
	private void updateReservConnResp(String reservSt, int reservNo, String centerReservId, String confirmReservDay,
			ReservRequest request) throws VitaException {

		UpdtReservConnResponse updtReservConnRespVo = new UpdtReservConnResponse();
		updtReservConnRespVo.setReservSt(reservSt); // 예약확정
		updtReservConnRespVo.setReservNo(reservNo);
		updtReservConnRespVo.setCenterReservId(centerReservId);
		updtReservConnRespVo.setReservDay(confirmReservDay);
		updtReservConnRespVo.setLastPath(request.getLastPath());
		updtReservConnRespVo.setLastModifier(request.getLastModifier());

		try {
			mapper.updateReservConnResp(updtReservConnRespVo);
		} catch (Exception e) {
			// 전역 변수 사용
			updateReservStatus("90", reservNo, request.getLastPath(), request.getLastModifier());
			restoreUsageAmount(globalOriginAmount, globalOriginRosterNo);
			log.warn("DB error (updtCenterReservId)", e);
			throw new VitaException(VitaCode.DATABASE_ERROR); // DB error
		}
	}

	/**
	 * 유투바이오에서 예약 가능한 시간을 조회하여 설정
	 * 
	 * @throws VitaException
	 */
	private void validateAndFetchAvailableTime(ReservRequest request, ExtIntegrationDto commonExtInfo,
			CheckupProductDto checkupProductVo, RegReservDto regReservVo) throws VitaException {
		if (!ValidUtil.isEmpty(commonExtInfo) && !ValidUtil.isEmpty(checkupProductVo.getCenterProductNo())) {
			try {
				String reservTime = getAvailableCheckupTimesFromU2Bio(request, commonExtInfo);
				regReservVo.setReservTime(reservTime);
			} catch (VitaException e) {
				log.warn("유투바이오에서 예약 가능 시간 조회 실패: {}", e.getMessage());
				throw e; // 상위 호출 메서드에서 처리할 수 있도록 다시 던짐
			}
		}
	}

	/**
	 * 예약데이터를 유투바이오에 전송(유투바이오 예약)
	 * 
	 * @throws VitaException
	 */
	private String sendReservationToU2Bio(RegReservDto regReservVo, ExtIntegrationDto commonExtInfo,
			List<ReservTestItemDto> testItemInfoList) throws VitaException {
		// 유투바이오 수검자 차트 번호
		String patientNo;
		// 수검자 정보 생성 및 차트번호 조회
		U2bioPatientDto patientInfo = new U2bioPatientDto();
		Map<String, String> genPatientInfo = generatePatientInfo(regReservVo.getBirthDay(), regReservVo.getGenderCd());

		patientInfo.setName(regReservVo.getRosterName());

		patientInfo.setSocialNumber(genPatientInfo.get("socialNumber"));
		patientInfo.setBirthDate(genPatientInfo.get("birthDate"));
		patientInfo.setGender(genPatientInfo.get("genderCd"));
		patientInfo.setCellNumber(regReservVo.getMobileNo());
		patientInfo.setPhoneNumber(regReservVo.getPhoneNo());
		patientInfo.setZoneCode(regReservVo.getZipCd());
		patientInfo.setRoadAddress(regReservVo.getAddress());
		patientInfo.setAddressDetail(regReservVo.getAddressDetail());
		patientInfo.setEMail(regReservVo.getEmail());

		try {
			u2bioService.setCommonExtInfo(commonExtInfo);
			patientNo = u2bioService.getPatientInfo(patientInfo);
			log.debug("{patientNo}" + patientNo);
			patientInfo.setPatientNo(patientNo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("유투바이오 API 연동 실패 : 차트번호 조회ㅇ");
			log.error(e.getMessage());
			throw new VitaException(VitaCode.DATABASE_ERROR); // DB 에러
		}

		// 유투바이오 예약생성 API 전송용 Data 생성
		ReserveDataDto reserveData = new ReserveDataDto();
		reserveData.setPatientInfo(patientInfo); // patientInfo
		reserveData.setCheckupYear(Integer.parseInt(regReservVo.getPolicyYear()));
		reserveData.setCompanyId(regReservVo.getCustomerId());
		reserveData.setCompanyDepartment(regReservVo.getCustomerName());
		reserveData.setUserId(commonExtInfo.getItgId());
		reserveData.setMobileRemark(regReservVo.getRequestContents());
		reserveData.setReserveMemo(regReservVo.getReservRequestContents());
		// 날짜 변환 (yyyyMMdd -> yyyy-MM-dd)
		reserveData.setApptDate(
				LocalDate.parse(regReservVo.getReserv1stHopeDay(), DateTimeFormatter.BASIC_ISO_DATE).toString());
		// 시간 변환 (HHmm -> HH:mm)
		reserveData.setApptTime(LocalTime.parse(regReservVo.getReservTime(), DateTimeFormatter.ofPattern("HHmm"))
				.format(DateTimeFormatter.ofPattern("HH:mm")));

		EmployRelationType relationType = EmployRelationType.fromCode(regReservVo.getEmployRelationType());
		String relationTypeName = relationType.getDescription();
		if (!"00".equals(regReservVo.getEmployRelationType())) {
			CheckupSuperRosterDto superRosterInfo = mapper.selectSuperRosterInfo(regReservVo.getCheckupRosterNo());
			relationTypeName = StringUtils.isEmpty(superRosterInfo.getRosterName()) ? relationType.getDescription()
					: superRosterInfo.getRosterName() + " " + relationType.getDescription();
		}

		reserveData.setRelationShips(relationTypeName);

		List<ReserveDataDto.BasisTest> basisTestList = new ArrayList<>();
		ReserveDataDto.BasisTest basisTestInfo = new ReserveDataDto.BasisTest();
		basisTestInfo.setTestName(regReservVo.getCheckupProductTitle());
		basisTestInfo.setTestCode(regReservVo.getCenterProductNo());
		basisTestInfo.setBasisSupportPaymentAmount(regReservVo.getCompanySupportAmount());
		basisTestList.add(basisTestInfo);

		List<ReserveDataDto.SelectionTest> selectionTestList = testItemInfoList.stream()
				.filter(s -> s.getChoiceGroupNo() > 0 && s.getChoiceGroupNo() <= 5).map(testItem -> {
					ReserveDataDto.SelectionTest selectionTest = new ReserveDataDto.SelectionTest();
					selectionTest.setTestName(testItem.getTestItemName());
					selectionTest.setTestCode(testItem.getTestItemCd());
					selectionTest.setSelectionName("선택" + getAlphabet(testItem.getChoiceGroupNo()));
					return selectionTest;
				}).collect(Collectors.toList());

		List<ReserveDataDto.AdditionalTest> additionalTest = testItemInfoList.stream()
				.filter(s -> "Y".equals(s.getAddCheckupYn()) || s.getChoiceGroupNo() == 9).map(testItem -> {
					ReserveDataDto.AdditionalTest basisTest = new ReserveDataDto.AdditionalTest();
					basisTest.setTestName(testItem.getTestItemName());
					basisTest.setTestCode(testItem.getTestItemCd());
					basisTest.setTestPaymentAmount(testItem.getAddTestPrice());
					return basisTest;
				}).collect(Collectors.toList());

		reserveData.setTotalPaymentAmount(regReservVo.getSelfPayAmount() + regReservVo.getCompanySupportAmount());
		reserveData.setBasisTest(basisTestList);
		reserveData.setSelectionTest(selectionTestList);
		reserveData.setAdditionalTest(additionalTest);

		if (!StringUtils.isEmpty(regReservVo.getResultReceiveWayCd())) {
			reserveData.setReceiptWayId(getReceiptCd(regReservVo.getResultReceiveWayCd()));
		}

		try {
			u2bioService.setCommonExtInfo(commonExtInfo);
			String centerReservId = u2bioService.setReservation(reserveData, HttpMethod.POST);

			log.debug(centerReservId);
			log.debug(">>>>>>>>>>>>");
			log.debug(reserveData.toString());

			return centerReservId;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn(e.getMessage());
			log.warn("유투바이오 API 연동 실패 : 예약 생성");
			throw new VitaException(VitaCode.DATABASE_ERROR); // DB 에러
		}
	}

	/**
	 * 유투바이오에서 예약가능일 유무 확인
	 * 
	 * @param request
	 * @param commonExtInfo
	 * @return
	 * @throws VitaException
	 */
	public String getAvailableCheckupTimesFromU2Bio(ReservRequest request, ExtIntegrationDto commonExtInfo)
			throws VitaException {
		log.debug("유투바이오 연동 정보 : " + commonExtInfo);
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		U2bioReservAvailableDateRequest appointmentDatesPo = new U2bioReservAvailableDateRequest();
		String startDate = request.getReserv1stHopeDay().substring(0, 8);
		LocalDate calStartDay = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
		String amPm = request.getReserv1stHopeDay().substring(request.getReserv1stHopeDay().length() - 2,
				request.getReserv1stHopeDay().length());

		appointmentDatesPo.setStartDate(calStartDay.format(outputFormatter));
		appointmentDatesPo.setAmPm(amPm);
		List<String> itemList = request.getCheckupItemList().stream().map(ReservRequest.CheckupItemList::getTestItemCd)
				.filter(item -> item != null && !item.isEmpty()).collect(Collectors.toList());
		appointmentDatesPo.setTestCodes(String.join(",", itemList));

		try {
			u2bioService.setCommonExtInfo(commonExtInfo);
			String reservTime = u2bioService.getReserveAvailableTime(appointmentDatesPo);
			log.debug("{reservTime}" + reservTime);
			if (StringUtils.isEmpty(reservTime)) {
				throw new VitaException(VitaCode.NO_AVAILABLE_RESERV_TIME); // DB 에러
			}
			return reservTime;
		} catch (Exception e) {
			log.warn(e.getMessage());
			log.warn("유투바이오 API 연동 실패");
			throw new VitaException(VitaCode.API_ERROR); // DB 에러
		}
	}

	/**
	 * 예약 데이터를 저장하고 예약 번호를 반환
	 * 
	 * @throws VitaException
	 */
	private int saveReservationData(RegReservDto regReservVo) throws VitaException {
		try {
			mapper.insertReserv(regReservVo); // HC_RESERV
			return regReservVo.getReservNo();
		} catch (Exception e) {
			log.error("DB 오류 (예약 등록 실패): {}", e.getMessage(), e);
			throw new VitaException(VitaCode.DATABASE_ERROR, "예약 데이터 저장 중 오류 발생");
		}
	}

	/**
	 * 대상자의 휴대폰 번호를 업데이트 (등록되지 않은 경우)
	 * 
	 * @throws VitaException
	 */
	private void updateMobileNumber(CheckupRosterDto hcCheckupRosterResponse, RegReservDto regReservVo)
			throws VitaException {
		if ("N".equals(hcCheckupRosterResponse.getMobileNoYn()) && "".equals(hcCheckupRosterResponse.getMobileNo())) {
			try {
				mapper.updateCheckupRosterMobileNo(regReservVo);
			} catch (Exception e) {
				log.error("DB 오류 (휴대폰 번호 업데이트 실패): {}", e.getMessage(), e);
				throw new VitaException(VitaCode.DATABASE_ERROR, "휴대폰 번호 업데이트 중 오류 발생");
			}
		}
	}

	/**
	 * 결제 정보 업데이트
	 * 
	 * @throws VitaException
	 */
	private void updatePaymentInfo(ReservRequest request, int reservNo) throws VitaException {
		if (!ValidUtil.isEmpty(request.getPayNo())) {
			try {
				UpdatePayReservNoRequest updatePayReservNoRequest = new UpdatePayReservNoRequest();
				updatePayReservNoRequest.setReservNo(reservNo);
				updatePayReservNoRequest.setPayNo(request.getPayNo());
				payService.updatePayReservNo(updatePayReservNoRequest);
			} catch (Exception e) {
				log.error("DB 오류 (결제 정보 업데이트 실패): {}", e.getMessage(), e);
				throw new VitaException(VitaCode.DATABASE_ERROR, "결제 정보 업데이트 중 오류 발생");
			}
		}
	}

	/**
	 * 케파 갱신처리
	 * 
	 * @throws VitaException
	 */
	private void updateCapacity(ReservRequest request) throws VitaException {

		// 케파 차감 로직 (2023-04-14 정성욱 선임 요청)
		// - 결제 중 케파 마감 상황 방지를 위해 "결제대기 상태"에서도 케파 차감하도록 수정
		// - 협력병원의 경우 케파 차감 적용
		// - 00 타입 협력병원: 1차, 2차 희망일자의 케파를 차감

		String groupNo = getGroupCapaNo(request.getPartnerCenterId(), request.getCustomerId());
		String deferYn = !ValidUtil.isEmpty(request.getDeferReservHopeDay()) ? "Y" : "N";
		List<String> testItemInfoStringList = mapper.selectTestItemCdStringList(request.getReservNo());
		/* 채용검진 대상자는 케파차감 안함 */
		if (!"40".equals(request.getCheckupDivCd())) {
			// 1,2차 검진일이 동일 할 경우 케파 1개만 차감
			if (request.getReserv1stHopeDay().equals(request.getReserv2ndHopeDay())) {
				if (groupNo != null) {
					newChangeCapaAboutPlaceEquipSepecial("minus", request.getReserv1stHopeDay(), "",
							request.getDeferReservHopeDay(), request.getPartnerCenterId(), request.getCheckupRosterNo(),
							testItemInfoStringList, request.getCheckupPlaceId(), deferYn, "R", groupNo);
				} else {
					newChangeCapaAboutPlaceEquipSepecial("minus", request.getReserv1stHopeDay(), "",
							request.getDeferReservHopeDay(), request.getPartnerCenterId(), request.getCheckupRosterNo(),
							testItemInfoStringList, request.getCheckupPlaceId(), deferYn, "R");
				}
			} else {
				// 1차케파 차감시에만 분리검진 장비 케파 차감
				if (groupNo != null) {
					newChangeCapaAboutPlaceEquipSepecial("minus", request.getReserv1stHopeDay(),
							request.getReserv2ndHopeDay(), request.getDeferReservHopeDay(),
							request.getPartnerCenterId(), request.getCheckupRosterNo(), testItemInfoStringList,
							request.getCheckupPlaceId(), deferYn, "R", groupNo);
				} else {
					newChangeCapaAboutPlaceEquipSepecial("minus", request.getReserv1stHopeDay(),
							request.getReserv2ndHopeDay(), request.getDeferReservHopeDay(),
							request.getPartnerCenterId(), request.getCheckupRosterNo(), testItemInfoStringList,
							request.getCheckupPlaceId(), deferYn, "R");
				}
			}
		}
	}

	/**
	 * 검진 항목 등록
	 * 
	 * @throws VitaException
	 */
	public RegReservTestItemResponse regReservTestItem(ReservRequest reservRequest, List<String> holdTestItemList)
			throws VitaException {

		RegReservTestItemRequest request = new RegReservTestItemRequest(); // 예약검사 항목
		/* 3. 예약검사항목 등록 */
		request.setPartnerCenterId(reservRequest.getPartnerCenterId());
		request.setReservNo(reservRequest.getReservNo());
		request.setDeferReservNo(reservRequest.getDeferReservNo());
		request.setCHECKUP_PRODUCT_NO(reservRequest.getCheckupProductNo());
		List<RegReservTestItemRequest.CheckupItemList> itemList = reservRequest.getCheckupItemList().stream()
				.map(requestItem -> {
					RegReservTestItemRequest.CheckupItemList item = new RegReservTestItemRequest.CheckupItemList();
					item.setCheckupItemCd(requestItem.getCheckupItemCd());
					item.setItemDivCd(requestItem.getItemDivCd());
					item.setChoiceBundleNo(requestItem.getChoiceBundleNo());
					item.setChoiceGroupNo(requestItem.getChoiceGroupNo());
					item.setOnpayAmount(requestItem.getOnpayAmount());
					item.setSelfPayAmount(requestItem.getSelfPayAmount());
					item.setPkgCd(requestItem.getPkgCd());

					if ("30".equals(requestItem.getItemDivCd())) {
						item.setChoiceGroupNo(9);
						item.setNonPriceYN(requestItem.getNonPriceYN());
						item.setCustomPriceYN(requestItem.getCustomPriceYN());
						item.setCustomTotalPrice(requestItem.getCustomTotalPrice());
						item.setCustomCompanyPrice(requestItem.getCustomCompanyPrice());
						item.setCustomSelfPrice(requestItem.getCustomSelfPrice());
					}
					return item;
				}).collect(Collectors.toList());
		request.setCheckupItemList(itemList);

		/* 3. 검진항목관련 데이터 셋 생성 */
		List<RegReservTestItemResponse.TestItemList> testItemInfoList = new ArrayList<>(); // 검사항목정보리스트
		List<String> checkupItemList = new ArrayList<>(); // 검진항목리스트

		for (int i = 0; i < request.getCheckupItemList().size(); i++) { // 검진항목에 대하여

			// 변수 정의
			String nowCheckupItemCd = request.getCheckupItemList().get(i).getCheckupItemCd();
			String nowItemDivCd = request.getCheckupItemList().get(i).getItemDivCd();
			int nowChoiceGroupNo = request.getCheckupItemList().get(i).getChoiceGroupNo();
			int nowChoiceBundleNo = request.getCheckupItemList().get(i).getChoiceBundleNo();
			String deferCheckupYN = request.getCheckupItemList().get(i).getDeferCheckupYN();
			String nonPriceYN = request.getCheckupItemList().get(i).getNonPriceYN();
			String customPriceYN = request.getCheckupItemList().get(i).getCustomPriceYN();
			int customTotalPrice = request.getCheckupItemList().get(i).getCustomTotalPrice();
			int customCompanyPrice = request.getCheckupItemList().get(i).getCustomCompanyPrice();
			int customSelfPrice = request.getCheckupItemList().get(i).getCustomSelfPrice();
			String pkgCd = request.getCheckupItemList().get(i).getPkgCd();

			/* 3-1. 검사항목코드 조회 */
			List<GetTestItemCdDto> testItemCdVoList = getTestItemCd(request.getPartnerCenterId(), nowCheckupItemCd,
					request.getReservNo(), request.getCHECKUP_PRODUCT_NO(), nowChoiceGroupNo);
			RegReservTestItemResponse.TestItemList testItemInfoDetail = new RegReservTestItemResponse.TestItemList();

			// 3-2. 검사항목리스트 생성
			for (GetTestItemCdDto vo : testItemCdVoList) {
				testItemInfoDetail.setCheckupItemCd(vo.getCheckupItemCd());
				testItemInfoDetail.setTestItemCd(vo.getTestItemCd());
				testItemInfoDetail.setItemDivCd(nowItemDivCd);
				testItemInfoDetail.setChoiceGroupNo(nowChoiceGroupNo);
				testItemInfoDetail.setChoiceBundleNo(nowChoiceBundleNo);
				testItemInfoDetail.setPkgCd(pkgCd);
				testItemInfoDetail.setAddTestPrice(vo.getAddTestPrice());
				testItemInfoDetail.setDeferCheckupYN(deferCheckupYN);
				testItemInfoDetail.setNonPriceYN(nonPriceYN);
				testItemInfoDetail.setCustomPriceYN(customPriceYN);
				testItemInfoDetail.setCustomTotalPrice(customTotalPrice);
				testItemInfoDetail.setCustomCompanyPrice(customCompanyPrice);
				testItemInfoDetail.setCustomSelfPrice(customSelfPrice);
				testItemInfoList.add(testItemInfoDetail);
			}

			// 3-3. 검진항목리스트 생성
			checkupItemList.add(nowCheckupItemCd);

		} // 검진항목for문종료

		/* 5-1. 예약검사항목DB등록 */
		for (int i = 0; i < request.getCheckupItemList().size(); i++) { // 검진항목리스트에 대하여

			// 맵핑항목 조회
			GetTestItemCdDto getTestItemCdVo = new GetTestItemCdDto();
			getTestItemCdVo.setPartnerCenterId(request.getPartnerCenterId());
			getTestItemCdVo.setCheckupItemCd(request.getCheckupItemList().get(i).getCheckupItemCd());
			List<ReservTestItemDto> mappingCdList = mapper.selectMappingCd(getTestItemCdVo); // HC_TEST_ITEM_CD

			if (ValidUtil.isEmpty(mappingCdList)) { // 맵핑 조회 결과가 비었다면
				// 맵핑이 안된 상황
				log.error("검진-검사항목 간 맵핑이 되지 않음. 검진항목코드 : {}, 병원아이디 : {}",
						request.getCheckupItemList().get(i).getCheckupItemCd(), request.getPartnerCenterId());
				throw new VitaException("VR000", "검진-검사항목 간 맵핑 오류(L:334)"); // 유효하지 않은 값
			}

			for (ReservTestItemDto mappingCd : mappingCdList) { // 검진항목과 맵핑된 검사항목에 대하여 (검진1:검사N)

				// vo setting
				ReservTestItemDto reservTestItemDto = new ReservTestItemDto();
				reservTestItemDto.setReservNo(request.getReservNo()); // 예약번호
				if (request.getCheckupItemList().get(i).getCheckupItemCd().equals(NORMAL_COLON) // 검진항목 중 대장이 있거나
						|| request.getCheckupItemList().get(i).getCheckupItemCd().equals(SLEEP_COLON)) { // 검진항목 중
																											// 대장(수면)이
																											// 있을 경우
					reservTestItemDto.setDeferCheckupReservNo(request.getDeferReservNo()); // 연기검진번호
				}
				reservTestItemDto.setCheckupItemCd(mappingCd.getCheckupItemCd()); // 검진항목코드
				reservTestItemDto.setTestItemCd(mappingCd.getTestItemCd()); // 검사항목코드
				reservTestItemDto.setChoiceGroupNo(request.getCheckupItemList().get(i).getChoiceGroupNo()); // 묶음번호
				reservTestItemDto.setChoiceBundleNo(request.getCheckupItemList().get(i).getChoiceBundleNo()); // 번들번호
				reservTestItemDto.setItemDivCd(request.getCheckupItemList().get(i).getItemDivCd()); // 항목구분코드 칼럼 추가됨
																									// (202302~)
				reservTestItemDto.setDeferCheckupYn(request.getCheckupItemList().get(i).getDeferCheckupYN());
				reservTestItemDto.setReplaceItem(request.getCheckupItemList().get(i).getReplaceItem());
				/* 검사 보류 여부 */
				for (String testItem : holdTestItemList) {
					if (testItem.equals(mappingCd.getTestItemCd())) {
						reservTestItemDto.setHoldYn("Y");
					}
				}
				String addCheckupYn = "";
				if (request.getCheckupItemList().get(i).getItemDivCd().equals("20") // 선택항목이라면
						|| request.getCheckupItemList().get(i).getItemDivCd().equals("10")) { // 기본항목이라면
					addCheckupYn = "N";
				}
				if (request.getCheckupItemList().get(i).getItemDivCd().equals("30")) { // 추가항목이라면
					addCheckupYn = "Y";
					reservTestItemDto.setChoiceGroupNo(9);
				}
				reservTestItemDto.setAddCheckupYn(addCheckupYn); // 추가검사여부
				reservTestItemDto.setNonPriceYn(request.getCheckupItemList().get(i).getNonPriceYN());
				reservTestItemDto.setCustomPriceYn(request.getCheckupItemList().get(i).getCustomPriceYN());
				if ("Y".equals(addCheckupYn) || "Y".equals(request.getCheckupItemList().get(i).getNonPriceYN())
						|| "Y".equals(request.getCheckupItemList().get(i).getCustomPriceYN())) {
					reservTestItemDto.setCustomTotalPrice(request.getCheckupItemList().get(i).getCustomTotalPrice());
					reservTestItemDto
							.setCustomCompanyPrice(request.getCheckupItemList().get(i).getCustomCompanyPrice());
					reservTestItemDto.setCustomSelfPrice(request.getCheckupItemList().get(i).getCustomSelfPrice());
				}

				// db insert
				try {
					mapper.insertReservTestItem(reservTestItemDto); // HC_RESERV_TEST_ITEM
				} catch (DuplicateKeyException e) {
					log.debug("(regReservTestItem) 해당 검사항목 이미 해당 예약번호로 db 인입되어 스킵");
					continue;
				} catch (Exception e) {
					log.error("DB error (regReservTestItem)", e);
					throw new VitaException(VitaCode.DATABASE_ERROR);
				}
			} // 검진-검사항목 맵핑 for문

		}

		RegReservTestItemResponse response = new RegReservTestItemResponse();
		response.setTestItemInfoList(testItemInfoList);
		return response;
	}

	/**
	 * 사전 요청값 검증
	 * 
	 * @param request
	 * @param checkupProductDto
	 * @param checkupRosterDto
	 * @throws VitaException
	 */
	private void validateRequest(ReservRequest request, CheckupRosterDto checkupRosterDto,
			CheckupProductDto checkupProductDto) throws VitaException {
		
		ValidUtil.validNull(request.getCustomerId(), request.getPolicyYear(), request.getPartnerCenterId(),
				request.getCheckupProductNo(), request.getCheckupDivCd(), request.getReserv1stHopeDay(),
				request.getForceReservYn());
		
		if ("60".equals(checkupRosterDto.getEmployRelationType())) { // 임직원 관계타입이 미지정일 경우
            log.error("예약전문에 미지정자가 들어옴");
            throw new VitaException("VR000", "수검자와 임직원의 관계가 부정확합니다."); //유효하지않은 값
        }
		
		String startDt = checkupProductDto.getReservStartDt();
		String endDt = checkupProductDto.getReservEndDt();
		
		if (ValidUtil.isEmpty(startDt) || ValidUtil.isEmpty(endDt)) {
            log.warn("DB에 해당하는 날짜 정보가 없음");
            throw new VitaException("VR000", "예약 시작/종료일시가 부정확합니다."); // 유효하지 않은 값
        }
		
		
		
		
	}

	/**
	 * 결과지 수령 주소값 세팅
	 * 
	 * @param request
	 */
	private void updateMissingAddresses(ReservRequest request) {
		if (ValidUtil.isEmpty(request.getZipCd()) && ValidUtil.isEmpty(request.getAddress())
				&& ValidUtil.isEmpty(request.getAddressDetail())) {
			request.setZipCd(request.getSuppliesZipCd());
			request.setAddress(request.getSuppliesAddress());
			request.setAddressDetail(request.getSuppliesAddressDetail());
		}

		if (ValidUtil.isEmpty(request.getSuppliesZipCd()) && ValidUtil.isEmpty(request.getSuppliesAddress())
				&& ValidUtil.isEmpty(request.getSuppliesAddressDetail())) {
			request.setSuppliesZipCd(request.getZipCd());
			request.setSuppliesAddress(request.getAddress());
			request.setSuppliesAddressDetail(request.getAddressDetail());
		}
	}

	/**
	 * 검진 대상자 조회
	 * 
	 * @param request
	 * @return
	 * @throws VitaException
	 */
	private CheckupRosterDto fetchCheckupRosterInfo(ReservRequest request) throws VitaException {
		try {
			CheckupRosterRequest hcCheckupRosterRequest = new CheckupRosterRequest();
			hcCheckupRosterRequest.setCheckupRosterNo(request.getCheckupRosterNo());

			CheckupRosterDto checkupRosterDto = new CheckupRosterDto();
			checkupRosterDto = mapper.selectCheckupRoster(hcCheckupRosterRequest);

			/*
			 * 검진대상자 조회 결과 없을 경우?(어드민 요청이거나 기타 유입경로일 경우) 선등록 안된 로스터일 경우 등록 후 다시 조회하여 가져와야 함
			 */

			if (ValidUtil.isEmpty(checkupRosterDto) // 대상자 정보가 없을 경우
					&& request.getAdminReqYn().equals("Y") // 어드민 요청일 경우
					&& !ValidUtil.isEmpty(request.getRegRosterRequest())) { // 대상자등록요청값이 유효할 경우

				if ("60".equals(request.getRegRosterRequest().getEmployRelationType())) { // 요청등록대상자의 타입이 미지정자일 경우
					log.error("예약전문에 미지정자가 들어옴");
					throw new VitaException(VitaCode.REQUIRED_VALUE_FAIL); // 유효하지않은 값
				}

				RosterResponse response = registRoster(request);
				hcCheckupRosterRequest.setCheckupRosterNo(response.getCheckupRosterNo());
				// 등록된 대상자 정보 조회
				checkupRosterDto = mapper.selectCheckupRoster(hcCheckupRosterRequest);
			}

			if (ValidUtil.isEmpty(checkupRosterDto.getCheckupRosterNo())) { // 대상자 정보가 없을 경우
				log.warn("미등록 대상자로 진행 불가, 요청값:{}", request);
				throw new VitaException(VitaCode.REQUIRED_VALUE_FAIL); // 유효하지 않은 값
			}

			return checkupRosterDto;
		} catch (Exception e) {
			log.error("검진 대상자 정보 조회 오류", e);
			throw new VitaException(VitaCode.DATABASE_ERROR);
		}
	}

	/**
	 * 검진상품 조회
	 * 
	 * @param request
	 * @return
	 * @throws VitaException
	 */
	private CheckupProductDto fetchCheckupProductInfo(ReservRequest request) throws VitaException {
		try {
			CheckupProductDto checkupProductVo = new CheckupProductDto();
			checkupProductVo.setCustomerId(request.getCustomerId());
			checkupProductVo.setCheckupProductNo(request.getCheckupProductNo());
			checkupProductVo.setPolicyYear(request.getPolicyYear());
			return mapper.selectCheckupProduct(checkupProductVo);
		} catch (Exception e) {
			log.error("검진 상품 정보 조회 오류", e);
			throw new VitaException(VitaCode.DATABASE_ERROR);
		}
	}

	/**
	 * 연계 협력병원 조회
	 * 
	 * @param request
	 * @return
	 * @throws VitaException
	 */
	private CenterConnecterInfoDto fetchCenterConnecterInfo(ReservRequest request) throws VitaException {
		CenterConnecterInfoDto connecterInfoVo = new CenterConnecterInfoDto();
		connecterInfoVo.setCenterId(request.getPartnerCenterId());
		connecterInfoVo.setConnecterId("center.regreserv");
		return mapper.selectHcCenterConnecter(connecterInfoVo);
	}

	/**
	 * 외부 EMR 연동정보 조회
	 * 
	 * @param request
	 * @return
	 * @throws VitaException
	 */
	private ExtIntegrationDto fetchExternalIntegrationInfo(ReservRequest request) throws VitaException {
		return mapper.selectExtConnection(request.getPartnerCenterId());
	}

	/**
	 * 검진예약(HC_RESERV INSERT)
	 * 
	 * @throws VitaException
	 */
	private int processReservation(RegReservDto regReservVo, ReservRequest request,
			CheckupRosterDto hcCheckupRosterResponse, CheckupProductDto checkupProductVo,
			ExtIntegrationDto commonExtInfo) throws VitaException {
		validateAndFetchAvailableTime(request, commonExtInfo, checkupProductVo, regReservVo);
		updateCompanySupportAmt(hcCheckupRosterResponse.getCheckupRosterNo(), request);
		return saveReservationData(regReservVo);
	}

	/**
	 * 연기 검진항목 등록
	 * 
	 * @param request
	 * @throws VitaException
	 */
	private int processDeferredCheckup(ReservRequest request) throws VitaException {
		return registDeferredCheckup(request);
	}

	/**
	 * 예약 보류된 검진항목 상태값 업데이트
	 * 
	 * @param reservNo
	 * @param holdTestItemList
	 * @throws VitaException
	 */
	private void updateHoldItemStatus(int reservNo, List<String> holdTestItemList) throws VitaException {
		/* 예약보류 항목 여부 업데이트 */
		try {
			mapper.updateReservTestItem(reservNo, holdTestItemList);
		} catch (Exception e) {
			log.error("예약보류 항목 여부 업데이트 실패 : " + e);
			throw new VitaException(VitaCode.DATABASE_ERROR);
		}
	}

	/**
	 * 심전도 항목 교체 대상 처리
	 * 
	 * @param reservNo
	 */
	public void updateEKGItem(int reservNo) {
		log.debug("심전도항목 검토");
		boolean isTarget = false;
		try {
			// 심전도항목 예약여부 검토
			if (mapper.selectEKGItem(reservNo) == 1) {
				// 심전도 전환조건 검토
				isTarget = checkEKGTarget(reservNo);
			}
		} catch (Exception e) {
			log.error("EKG find failed", e);
		}

		if (isTarget) {
			try {
				// 심전도 항목 업데이트
				mapper.updateEKGItem(reservNo);
				log.debug("심전도항목 전환(XC290 -> XC291)");
			} catch (Exception e) {
				log.error("EKG update failed", e);
			}
		}
	}

	/*
	 * 심전도 검사항목 대체 대상 검토
	 *
	 * 0. 상품검진구분: 종합검진 1. 만 60세 미만 여부 2. 심장초음파 미대상 여부 3. 수면내시경 미대상 여부 모두 충족 시 XC290
	 * -> XC291 변경
	 *
	 * @param 예약번호
	 *
	 */
	private boolean checkEKGTarget(int reservNo) {
		log.debug("심전도항목 교체대상 검토");

		// 종검구분
		if (mapper.selectDivCd(reservNo) == 0) {
			return false;
		}

		// 만 60세 미만 여부
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate today = LocalDate.now();
		String birthday = mapper.selectRosterBirthday(reservNo);
		LocalDate birthDate = LocalDate.parse(birthday, formatter);

		int age = Period.between(birthDate, today).getYears();
		if (today.getMonthValue() < birthDate.getMonthValue() || (today.getMonthValue() == birthDate.getMonthValue()
				&& today.getDayOfMonth() < birthDate.getDayOfMonth())) {
			age--;
		}

		if (age >= 60) {
			return false;
		}

		// 심장초음파 검사 여부
		if (mapper.selectEchoItem(reservNo) == 0) {
			return false;
		}

		// 수면내시경 검사 여부
		return mapper.selectSedationItem(reservNo) == 1;
	}

	public void insertAgreeTerms(AgreeTermsRequest dto) throws VitaException {
		try {
			mapper.insertAgreeTerms(dto);
		} catch (Exception e) {
			log.error("DB 등록실패", e);
			throw new VitaException(VitaCode.DATABASE_ERROR);
		}
	}

	/**
	 * 동의서 등록용 요청값 세팅
	 * 
	 * @param request
	 * @return
	 * @throws VitaException
	 */
	public AgreeTermsRequest buildTermsDTO(AgreeTermsDto request) throws VitaException {

		/* 1. 연도에 따른 동의서 아이디 조회 */
		Date nowDate = new Date();
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
		String now = formater.format(nowDate);

		/* 2. 등록일자에 따른 동의서 아이디 조회 */
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String, Object> rosterInfo;
		String firstTermsId = "";
		String secondTermsId = "";
		String thirdTermsId = "";
		map.put("regDate", now);
		map.put("serviceName", "필수동의1번");
		try {
			firstTermsId = mapper.selectAgreeTerms(map);
			map.put("serviceName", "필수동의2번");
			secondTermsId = mapper.selectAgreeTerms(map);
			map.put("serviceName", "선택동의3번");
			thirdTermsId = mapper.selectAgreeTerms(map);

			// 예약번호, 회원번호, 모바일번호 조회
			rosterInfo = mapper.selectRosterInfo(request.getCheckupRosterNo());

		} catch (Exception e) {
			log.error("DB 조회 실패", e);
			throw new VitaException(VitaCode.DATABASE_ERROR);
		}
		/* 3. DTO 설정 */
		AgreeTermsRequest dto = new AgreeTermsRequest();

		dto.setAgreeDt(now);
		dto.setFirstServiceInfoId(firstTermsId);
		dto.setSecondServiceInfoId(secondTermsId);
		dto.setThirdServiceInfoId(thirdTermsId);

		if (request.getFirstConsent() == null || request.getFirstConsent().isEmpty()) {
			dto.setFirstRequiredConsent("N");
		} else {
			dto.setFirstRequiredConsent(request.getFirstConsent());
		}

		if (request.getSecondConsent() == null || request.getSecondConsent().isEmpty()) {
			dto.setSecondRequiredConsent("N");
		} else {
			dto.setSecondRequiredConsent(request.getSecondConsent());
		}

		if (request.getThirdConsent() == null || request.getThirdConsent().isEmpty()) {
			dto.setThirdSelectiveConsent("N");
		} else {
			dto.setThirdSelectiveConsent(request.getThirdConsent());
		}

		dto.setCheckupRosterNo(request.getCheckupRosterNo());
		if (ValidUtil.isEmpty(request.getMobileNo())) {
			dto.setMobileNo((String) rosterInfo.get("mobileNo"));
		} else {
			dto.setMobileNo(request.getMobileNo());
		}
		dto.setAgreePath(request.getAgreePath());

		dto.setReservNo((int) rosterInfo.get("reservNo"));
		if (rosterInfo.get("userNo") == null || "".equals(rosterInfo.get("userNo"))) {
			dto.setUserNo(0);
		} else {
			dto.setUserNo((int) rosterInfo.get("userNo"));
		}
		dto.setEmployRelationType((String) rosterInfo.get("employRelationType"));
		log.debug("동의서등록 DTO: {}", dto);
		return dto;
	}

	/**
	 * 회사지원 사용금액 복원
	 * 
	 * @param globalOriginAmount
	 * @param globalOriginRosterNo
	 */
	public void restoreUsageAmount(int globalOriginAmount, int globalOriginRosterNo) {
		UpdtCompanySupAmtDto vo = new UpdtCompanySupAmtDto();
		vo.setRosterNo(globalOriginRosterNo);
		vo.setCompanySupportAmt(globalOriginAmount);
		mapper.updateCompanySupportUsageAmount(vo);
	}

	// 병원 연계
	public ReservResponse connToCenter(RegSpecialReservRequest regSpecialReservRequest, String targetUrl)
			throws VitaException {
		// 6-3. [연계] 병원 송신
		ReservResponse resp = new ReservResponse();
		try {
			resp = connection.request(regSpecialReservRequest, ReservResponse.class, targetUrl, HttpMethod.POST);
		} catch (VitaException e) {
			log.warn("통신 실패로 인한 예약 실패 to {}. 예약번호:{}", targetUrl, regSpecialReservRequest.getReservNo(), e);
			throw new VitaException(VitaCode.NETWORK_ERROR); // 연계실패
		}
		if (ValidUtil.isEmpty(resp)) { // 응답값이 비었을 경우
			log.warn("통신 응답값 null. 예약번호:{}", regSpecialReservRequest.getReservNo());
			throw new VitaException(VitaCode.NETWORK_ERROR); // 내부 에러
		}
		log.info("connecter 응답 내용 : {}", resp);

		return resp;
	}

	/**
	 * 프리미엄 케파 갱신 및 KICS 연계
	 * 
	 * @param request
	 * @param centerConnecterInfoDto
	 * @param connRegReservReq
	 * @param targetUrl
	 * @param path
	 * @param modifier
	 * @return
	 * @throws VitaException
	 */
	public ConnRegReservResponse connToCenter(ReservRequest request, List<ReservTestItemDto> testItemInfoList,
			CheckupRosterDto hcCheckupRosterResponse, RegReservDto regReservVo,
			CenterConnecterInfoDto centerConnecterInfoDto) throws VitaException {

		ConnRegReservRequest connRegReservReq = makeRegReservConnModel(request, testItemInfoList,
				hcCheckupRosterResponse, regReservVo);

		updateReservStatus("90", regReservVo.getReservNo(), request.getLastPath(), request.getLastModifier());
		log.warn("URL:{}, request:{}", centerConnecterInfoDto.getConnecterUrl(), connRegReservReq);

		request.setOriginReservNo(regReservVo.getReservNo());

		// 프리미엄 CAPA 적용여부 검수
		if ("Y".equals(connRegReservReq.getPremiumCapaYN())) {
			boolean isPremium = checkKICSReservDay(request);
			if (isPremium) { // 대상자가 프리미엄 CAPA 사용자인 경우
				// Premium CAPA 사용 -> PREMIUM CAPA 차감
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				LocalDate reserv1stHopeDay = LocalDate.parse(request.getReserv1stHopeDay().substring(0, 8), formatter);
				String reserv1stHopeTime = request.getReserv1stHopeDay().substring(8);
				String partnerCenterId = request.getPartnerCenterId();
				try {
					int updateRows = mapper.updateMinusPremiumCapa(String.valueOf(reserv1stHopeDay), reserv1stHopeTime,
							partnerCenterId);
					if (updateRows == 0) {
						log.info("프리미엄 CAPA 잔여수량 없음 -> 기존 CAPA 진행");
						mapper.updateRestorePremiumCapaYN(request);
						connRegReservReq.setPremiumCapaYN("N");
					}
				} catch (Exception e) {
					log.error("프리미엄 CAPA 차감 오류 -> 기존 CAPA 진행 ", e);
					mapper.updateRestorePremiumCapaYN(request);
					connRegReservReq.setPremiumCapaYN("N");
				}
			} else { // 프리미엄 CAPA 사용자가 아닌 경우
				// KICS CAPA 사용 -> Update(HC_RESERV) : PREMIUM_CAPA_YN = N
				mapper.updateRestorePremiumCapaYN(request);
				// 커넥터 모델 변경 -> premiumCapaYN = N
				connRegReservReq.setPremiumCapaYN("N");
			}
		}

		ConnRegReservResponse resp;
		try {
			resp = connection.request(connRegReservReq, ConnRegReservResponse.class,
					centerConnecterInfoDto.getConnecterUrl(), HttpMethod.POST);
		} catch (VitaException e) {
			updateReservStatus("90", connRegReservReq.getReservNo(), request.getLastPath(), request.getLastModifier());
			log.warn("통신 실패로 인한 예약 실패 to {}. 예약번호:{}", centerConnecterInfoDto.getConnecterUrl(),
					connRegReservReq.getReservNo(), e);
			throw new VitaException(VitaCode.NETWORK_ERROR);
		}
		if (ValidUtil.isEmpty(resp)) { // 응답값이 비었을 경우
			updateReservStatus("90", connRegReservReq.getReservNo(), request.getLastPath(), request.getLastModifier()); // 상태값
																														// 설정
																														// :
																														// 예약실패
			log.warn("통신 응답값 null. 예약번호:{}", connRegReservReq.getReservNo());
			throw new VitaException(VitaCode.NETWORK_ERROR); // 내부 에러
		}
		log.info("Connector 응답 내용 : {}", resp);

		// 일괄예약시 선택미정, 예약보류 케이스 추가 40 : 예약보류 , 41 : 선택미정
		if (resp.getReservSt().equals("40") || resp.getReservSt().equals("41")) {
			updateReservStatus(resp.getReservSt(), connRegReservReq.getReservNo(), request.getLastPath(),
					request.getLastModifier());
		} else if (!resp.getReservSt().equals("50")) {
			updateReservStatus("90", connRegReservReq.getReservNo(), request.getLastPath(), request.getLastModifier());
			log.error("연계 응답값이 정상이 아님. 예약번호:{}", connRegReservReq.getReservNo());
			throw new VitaException(VitaCode.NETWORK_ERROR);
		}

		if (!connRegReservReq.getReservNo().equals(resp.getReservNo())) {
			updateReservStatus("90", connRegReservReq.getReservNo(), request.getLastPath(), request.getLastModifier());
			log.error("요청했던 예약번호와 통신 후 응답받은 예약번호가 상이함, connRegReservReq.getReservNo() - {} / resp.ReservNo - {}",
					connRegReservReq.getReservNo(), resp.getReservNo());
			throw new VitaException(VitaCode.NETWORK_ERROR); // 유효하지 않은 값
		}

		return resp;
	}

	/* KICS 예약일자 여부 재조회 */
	private boolean checkKICSReservDay(ReservRequest request) throws VitaException {
		List<String> roomIdList = new ArrayList<>();
		List<String> checkupHolidayList = new ArrayList<>();
		for (ReservRequest.CheckupItemList item : request.getCheckupItemList()) {
			// 장비실 조회
			String testItemCd = mapper.selectTestItem(request.getPartnerCenterId(), item.getCheckupItemCd());
			String roomIdByCenter = "";
			List<String> bundleRoomIdByCenter = new ArrayList<>();
			GetRoomInfoDto roomVo = new GetRoomInfoDto();
			roomVo.setPartnerCenterId(request.getPartnerCenterId());
			roomVo.setTestItemCd(testItemCd);
			try {
				roomIdByCenter = mapper.selectRoomInfo(roomVo); // HC_CENTER_EQUIP_TEST
			} catch (Exception e) {
				log.warn("DB ERROR (getRoomInfo)", e);
				throw new VitaException(VitaCode.DATABASE_ERROR);
			}
			if (testItemCd.startsWith("GS")) {
				bundleRoomIdByCenter = mapper.selectBundleRoomInfo(testItemCd);
			}
			// 휴일 조회
			if ("HE104".equals(testItemCd) || "GS066".equals(testItemCd) || "GS239".equals(testItemCd)
					|| "HL324".equals(testItemCd)) {
				checkupHolidayList = mapper.selectCheckupHolidayInfo(); // HC_CENTER_HOLYDAY
			}

			if (!ValidUtil.isEmpty(roomIdByCenter)) { // 장비실이 유효한 경우
				log.debug("장비검사항목 있음 ");
				if ("G00006".equals(testItemCd)) {
					String[] roomIdByCenterArr = roomIdByCenter.split("-");
					for (String roomId : roomIdByCenterArr) {
						roomIdList.add(roomId.trim()); // 장비실목록에 추가
					}
				} else {
					roomIdList.add(roomIdByCenter); // 장비실목록에 추가
				}
			}
			if (!ValidUtil.isEmpty(bundleRoomIdByCenter)) { // 묶음코드 장비실이 유효한 경우
				log.debug("묶음장비검사항목 있음");
				roomIdList.addAll(bundleRoomIdByCenter);
			}
		}
		log.debug("장비검사항목 리스트 : " + roomIdList);

		// 장비실 중복제거
		List<String> roomList = roomIdList.stream().distinct().collect(Collectors.toList());
		String centerIdByCenter = mapper.selectCenterIdByCenter(request.getPartnerCenterId());

		// 연계 모델 셋팅
		ConnGetCheckupAbleDaysRequest connAbleDaysReq = new ConnGetCheckupAbleDaysRequest();
		connAbleDaysReq.setPartnerCenterId(request.getPartnerCenterId());
		connAbleDaysReq.setPartnerCenterIdByCenter(centerIdByCenter);
		connAbleDaysReq.setStartDt(request.getReserv1stHopeDay().substring(0, 8));
		connAbleDaysReq.setEndDt(request.getReserv1stHopeDay().substring(0, 8));
		if ("Y".equals(request.getSpecialCheckupYN())) {
			connAbleDaysReq.setSpecialCheckupYN(request.getSpecialCheckupYN());
		} else {
			connAbleDaysReq.setSpecialCheckupYN("N");
		}
		connAbleDaysReq.setEquipRoomIdByCenterList(roomList);

		// 연계
		ConnGetCheckupAbleDaysResponse resp;
		try {
			resp = connection.request(connAbleDaysReq, ConnGetCheckupAbleDaysResponse.class, "center/getreservabledays",
					HttpMethod.POST);
			if (ValidUtil.isEmpty(resp)) {
				log.warn("connector return valude is empty, 요청값:{}", connAbleDaysReq);
				throw new VitaException(VitaCode.NETWORK_ERROR);
			}
		} catch (VitaException e) {
			log.warn("[연계] 예약가능일자 통신 실패 ", e);
			throw new VitaException(VitaCode.NETWORK_ERROR);
		}

		List<String> reservDayList = resp.getReservAbleDayList();
		List<String> finalCheckupHolidayList = checkupHolidayList;
		if (!checkupHolidayList.isEmpty() && !reservDayList.isEmpty()) {
			reservDayList = reservDayList.stream()
					.filter(day -> finalCheckupHolidayList.stream().noneMatch(day::startsWith))
					.collect(Collectors.toList());
		}

		// CONNECTOR에서 AM/PM 둘다 가져와서 리체크
		boolean kicsReChk = reservDayList.isEmpty();

		if (!reservDayList.isEmpty()) {
			kicsReChk = !reservDayList.contains(request.getReserv1stHopeDay());
		}
		return kicsReChk;
	}

	/**
	 * 병원 연계용 VO Setting
	 * 
	 * @param request
	 * @param testItemInfoList
	 * @param hcCheckupRosterResponse
	 * @param regReservVo
	 * @return
	 * @throws VitaException
	 */
	public ConnRegReservRequest makeRegReservConnModel(ReservRequest request, List<ReservTestItemDto> testItemInfoList,
			CheckupRosterDto hcCheckupRosterResponse, RegReservDto regReservVo) throws VitaException {

		int reservNo = regReservVo.getReservNo(); // 병원측 병원아이디 조회
		String centerIdByCenter = getCenterIdByCenter(request.getPartnerCenterId(), reservNo, request.getLastPath(),
				request.getLastModifier());

		// 병원측 고객사 아이디 조회
		CenterCustomerInfoDto hcCenterCustomerInfoVo = new CenterCustomerInfoDto();
		String customerIdByCenter = "";
		hcCenterCustomerInfoVo.setCustomerId(request.getCustomerId());
		hcCenterCustomerInfoVo.setCenterId(request.getPartnerCenterId());
		try {
			customerIdByCenter = mapper.selectServiceCustomerId(hcCenterCustomerInfoVo); // HC_CENTER_CUSTOMER_INFO
																							// //CUSTOMER_ID_BY_CENTER
																							// //병원도
																							// 어떤 고객사가 요청을 한건지 알고 싶어한다.
																							// (상품제안요청 때 insert가 이미
																							// 되어있음)
		} catch (Exception e) {
			updateReservStatus("90", reservNo, request.getLastPath(), request.getLastModifier());
			log.error("DB error (getServiceCustomerId)", e);
			throw new VitaException(VitaCode.DATABASE_ERROR); // DB error
		}

		// 6-1. conn model setting
		ConnRegReservRequest connRegReservReq = new ConnRegReservRequest();
		if (request.getBatchReservYN().equals("Y")) {
			connRegReservReq.setHoldReservYN("Y");
		}
		connRegReservReq.setReservNo(reservNo);
		connRegReservReq.setCustomerId(request.getCustomerId());
		connRegReservReq.setPartnerCenterId(request.getPartnerCenterId());
		connRegReservReq.setCheckupProductNo(request.getCheckupProductNo());

		List<String> testItemList = new ArrayList<>();
		List<ConnRegReservRequest.TestItemList> itemList = new ArrayList<>();
		for (ReservTestItemDto item : testItemInfoList) {
			ConnRegReservRequest.TestItemList connItem = new ConnRegReservRequest.TestItemList();
			connItem.setItemDivCd(item.getItemDivCd());
			connItem.setTestItemCd(item.getTestItemCd());
			connItem.setChoiceBundleNo(item.getChoiceBundleNo());
			connItem.setChoiceGroupNo(item.getChoiceGroupNo());
			connItem.setPkgCd(item.getPkgCd());
			connItem.setNonPriceYN(item.getNonPriceYn());
			connItem.setCustomPriceYN(item.getCustomPriceYn());
			connItem.setCustomCompanyPrice(item.getCustomCompanyPrice());
			connItem.setCustomSelfPrice(item.getCustomSelfPrice());
			connItem.setPkgCd(item.getPkgCd());
			itemList.add(connItem);
			testItemList.add(connItem.getTestItemCd());
		}
		connRegReservReq.setCheckupItemList(itemList);

		connRegReservReq.setCustomerIdByCenter(customerIdByCenter);
		connRegReservReq.setPolicyYear(request.getPolicyYear());
		connRegReservReq.setCheckupRosterNo(request.getCheckupRosterNo());
		connRegReservReq.setPartnerCenterIdByCenter(centerIdByCenter);
		connRegReservReq.setProductIdByCenter(regReservVo.getCenterProductId());
		connRegReservReq.setCheckupDivCd(request.getCheckupDivCd());
		connRegReservReq.setSuperCheckupRosterNo(request.getSuperCheckupRosterNo());

		int selfPayAmount = request.getSelfPayAmount() - request.getSelfPayAddAmount();
		connRegReservReq.setSelfPayAmount(selfPayAmount < 0 ? 0 : selfPayAmount);

		int kicksCompanySupportAmount = request.getCompanySupportAmount();
		if (kicksCompanySupportAmount < 0) {
			kicksCompanySupportAmount = 0;
		}
		connRegReservReq.setCompanySupportAmount(kicksCompanySupportAmount);
		connRegReservReq.setOnpayAmount(regReservVo.getOnpayAmount());
		connRegReservReq.setOffpayExpectAmount(regReservVo.getOffpayExpectAmount());
		connRegReservReq.setRequestContents(request.getRequestContents());
		connRegReservReq.setReserv1stHopeDay(request.getReserv1stHopeDay());
		connRegReservReq.setReserv2ndHopeDay(request.getReserv2ndHopeDay());
		connRegReservReq.setResultReceiveWayCd(request.getResultReceiveWayCd());
		connRegReservReq.setForceReservYN(request.getForceReservYn());
		connRegReservReq.setAddress(request.getAddress());
		connRegReservReq.setZipCd(request.getZipCd());
		connRegReservReq.setAddressDetail(request.getAddressDetail());
		connRegReservReq.setSuppliesZipCd(request.getSuppliesZipCd());
		connRegReservReq.setSuppliesAddress(request.getSuppliesAddress());
		connRegReservReq.setSuppliesAddressDetail(request.getSuppliesAddressDetail());

		connRegReservReq.setRosterName(hcCheckupRosterResponse.getRosterName());
		connRegReservReq.setBirthday(hcCheckupRosterResponse.getBirthday());
		connRegReservReq.setGenderCd(hcCheckupRosterResponse.getGenderCd());
		connRegReservReq.setDomesticYN(hcCheckupRosterResponse.getDomesticYn());
		connRegReservReq.setNationalCd(hcCheckupRosterResponse.getNationalityCd());
		connRegReservReq.setSpecialCheckupYN(hcCheckupRosterResponse.getSpecialCheckupYn());
		connRegReservReq.setCustomAmountYN(request.getCustomAmountYN());
		connRegReservReq.setCustomCompanyAmount(request.getCustomCompanyAmount());
		connRegReservReq.setCustomSelfAmount(request.getCustomSelfAmount());
		connRegReservReq.setPregnancyYN(request.getPregnancyYN());
		connRegReservReq.setPossiblePregnancyYN(request.getPossiblePregnancyYN());
		connRegReservReq.setFeedingYN(request.getFeedingYN());
		connRegReservReq.setMensesYN(request.getMensesYN());
		connRegReservReq.setAnticoagulantYN(request.getAnticoagulantYN());
		connRegReservReq.setMelituriaYN(request.getMelituriaYN());
		connRegReservReq.setArteriotonyYN(request.getArteriotonyYN());
		connRegReservReq.setMedicationText(request.getMedicationText());
		connRegReservReq.setNephropathyYN(request.getNephropathyYN());
		connRegReservReq.setCardiopathyYN(request.getCardiopathyYN());
		connRegReservReq.setRenalFailureYN(request.getRenalFailureYN());
		connRegReservReq.setPeritionealDialysisYN(request.getPeritionealDialysisYN());
		connRegReservReq.setHemodialysisYN(request.getHemodialysisYN());
		connRegReservReq.setCaseHistoryText(request.getCaseHistoryText());
		if (request.getLastPath().equals("P") || request.getLastPath().equals("M")) {
			connRegReservReq.setVipYN(hcCheckupRosterResponse.getVipYn());
		} else {
			connRegReservReq.setVipYN(request.getVipYN());
		}
		connRegReservReq.setTransCustomerYN(request.getTransCustomerYN());
		connRegReservReq.setPreAcceptYN(request.getPreAcceptYN());

		// 임직원 관계정보 추가
		String employName = "";
		String employDepartment = "";
		String employNo = "";
		if (hcCheckupRosterResponse.getSuperCheckupRosterNo() == 0) { // 본인일 경우
			employName = hcCheckupRosterResponse.getRosterName();
			employDepartment = hcCheckupRosterResponse.getDepartment();
			employNo = hcCheckupRosterResponse.getEmployNo();
		} else { // 본인이 아닐 경우
			// 관계임직원 정보 조회하여 입력
			try {
				if (!"Y".equals(hcCheckupRosterResponse.getUnRegEmployYn())) {
					CheckupSuperRosterDto vo = mapper.selectSuperRosterInfo(request.getCheckupRosterNo());
					employName = vo.getRosterName();
					employDepartment = vo.getDepartment();
					employNo = vo.getEmployNo();
				} else {
					CheckupSuperRosterDto vo = mapper.selectUnRegEmployInfo(request.getCheckupRosterNo());
					employName = vo.getUnRegEmployName();
					employDepartment = vo.getUnRegEmployDepartment();
					employNo = vo.getUnRegEmployNo();
				}
			} catch (Exception e) {
				CheckupSuperRosterDto vo = mapper.selectSuperRosterInfo(request.getCheckupRosterNo());
				employName = vo.getRosterName();
				employDepartment = vo.getDepartment();
				employNo = vo.getEmployNo();
			}

		}
		connRegReservReq.setEmployNo(employNo);
		connRegReservReq.setDepartment(employDepartment);
		connRegReservReq.setEmployRelationType(hcCheckupRosterResponse.getEmployRelationType());
		connRegReservReq.setEmployName(employName);
		connRegReservReq.setUserDi(hcCheckupRosterResponse.getRosterDi());
		connRegReservReq.setUserCi(hcCheckupRosterResponse.getRosterCi());
		connRegReservReq.setDeferReservHopeDay(request.getDeferReservHopeDay());
		connRegReservReq.setMobileNo(regReservVo.getMobileNo());
		connRegReservReq.setPhoneNo(regReservVo.getPhoneNo());
		connRegReservReq.setEmail(regReservVo.getEmail());
		connRegReservReq.setSettlePrice(regReservVo.getSettlePrice());
		if (!ValidUtil.isEmpty(request.getDeferReservHopeDay())) {
			connRegReservReq.setDeferItem(request.getDeferItem());
		}
		connRegReservReq.setMigYN(regReservVo.getMigYn());
		connRegReservReq.setReservRequestContents(request.getReservRequestContents());
		// 프리미엄 CAPA
		if ("10".equals(request.getCheckupDivCd()) || "20".equals(request.getCheckupDivCd())) {
			if (ValidUtil.isEmpty(request.getDeferReservHopeDay())) {
				connRegReservReq.setPremiumCapaYN(findPremiumCapaTarget(request.getCheckupProductNo()));
			} else {
				connRegReservReq.setPremiumCapaYN("N");
			}
		} else {
			connRegReservReq.setPremiumCapaYN("N");
		}
		String premiumCapaYN = connRegReservReq.getPremiumCapaYN();
		mapper.updateReservPremiumCapa(premiumCapaYN, reservNo);
		request.setPremiumCapaYN(premiumCapaYN);
		return connRegReservReq;
	}

	// Premium CAPA 대상자 여부 확인 (상품 50만원 이상)
	public String findPremiumCapaTarget(int checkupProductNo) {
		// 신천지면 프리미엄 CAPA X 쿼리에 NOT IN 처리 20241119 이정호
		if (!mapper.selectPremiumCapaProduct(checkupProductNo)) {
			return "N";
		} else {
			return "Y";
		}
	}

	/**
	 * 병원측 병원ID 조회
	 * 
	 * @param partnerCenterId
	 * @param reservNo
	 * @param path
	 * @param modifier
	 * @return
	 * @throws VitaException
	 */
	public String getCenterIdByCenter(String partnerCenterId, int reservNo, String path, String modifier)
			throws VitaException {
		String centerIdByCenter = "";
		try {
			centerIdByCenter = mapper.selectCenterIdByCenter(partnerCenterId); // HC_CENTER_CUSTOMER_INFO
		} catch (Exception e) {
			updateReservStatus("90", reservNo, path, modifier);
			log.warn("DB error (getCenterPartnerCenterId), partnerCenterId : {}", partnerCenterId, e);
			throw new VitaException(VitaCode.DATABASE_ERROR); // DB error
		}

		return centerIdByCenter;
	}

	/**
	 * 예약상태 및 연기예약 상태 갱신
	 * 
	 * @param st
	 * @param reservNo
	 * @param path
	 * @param modifier
	 * @throws VitaException
	 */
	public void updateReservStatus(String st, int reservNo, String path, String modifier) throws VitaException {
		CommonUpdtReservStDto vo = new CommonUpdtReservStDto();
		vo.setReservSt(st);
		vo.setReservNo(reservNo);
		vo.setLastPath(path);
		vo.setLastModifier(modifier);
		try {
			mapper.updateReservSt(vo);
			mapper.updateDefferReservSt(vo);
		} catch (Exception e) {
			log.error("DB error (updtReservSt)", e);
			throw new VitaException(VitaCode.DATABASE_ERROR); // DB error
		}
	}

	private String getReceiptCd(String val) {
		String receiptWayId;
		switch (val) {
		case "10":
			receiptWayId = "4";
			break; // return "E-Mail 수신";
		case "20":
			receiptWayId = "1";
			break; // return "우편으로 수신";
		case "30":
			receiptWayId = "6";
			break; // return "검진기관 방문 수령";
		case "40":
			receiptWayId = "1";
			break; // return "전자등기 수령";
		case "50":
			receiptWayId = "3";
			break; // return "온라인 결과지";
		case "60":
			receiptWayId = "2";
			break; // return "택배 발송";
		default:
			receiptWayId = "";
			break;
		}
		return receiptWayId;
	}

	private String getAlphabet(Integer key) {
		String[] arr = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
				"T", "U", "V", "W", "X", "Y", "Z" };
		return arr[key - 1];
	}

	public Map<String, String> generatePatientInfo(String birthDate, String genderCd) {
		Map<String, String> result = new HashMap<>();
		// 생년월일 변환
		String bDate = birthDate.substring(0, 4) + "-" + birthDate.substring(4, 6) + "-" + birthDate.substring(6);

		// 성별 변환
		String gCode = "";
		switch (genderCd) {
		case "10":
			gCode = "M";
			break;
		case "20":
			gCode = "F";
			break;
		}

		// 주빈번호 생성
		String socialNumber = "";
		if (gCode != null && !gCode.isEmpty() && bDate != null && !bDate.isEmpty()) {
			String year = birthDate.substring(2, 4);
			String monthDay = birthDate.substring(4, 8);
			String genderDigit = "";

			int yearInt = Integer.parseInt(year);
			if (gCode.equals("M")) {
				genderDigit = (yearInt < 2000) ? "1" : "3";
			} else if (gCode.equals("F")) {
				genderDigit = (yearInt < 2000) ? "2" : "4";
			}

			socialNumber = year + monthDay + "-" + genderDigit + "000000";
		}

		result.put("birthDate", bDate);
		result.put("genderCd", gCode);
		result.put("socialNumber", socialNumber);
		return result;
	}

	/**
	 * 카카오 알림톡 전송 처리
	 * 
	 * @param serviceInfoId
	 * @param reservVo
	 * @param confirmReservDay
	 * @throws VitaException
	 */
	public void sendKakaoMsg(String serviceInfoId, RegReservDto reservVo, String confirmReservDay)
			throws VitaException {

		if (!"Y".equals(reservVo.getSmsYn())) {
			return;
		}

		String resultStr = ""; // 결과문장

		// 1. 검진기관명 조회
		String partnerCenterName = mapper.selectPartnerCenterName(reservVo.getPartnerCenterId());

		// 1. 템플릿 조회
		ServiceInfoDto hcServiceInfoVo = mapper.selectHcServiceInfo(serviceInfoId);
		String msg = hcServiceInfoVo.getInfoValue();

		// 2. 변수 설정
		msg = msg.replace("#{이름}", reservVo.getRosterName());
		msg = msg.replace("#{예약자명}", reservVo.getRosterName());
		msg = msg.replace("#{검진기관}", partnerCenterName);
		switch (serviceInfoId) {
		case "2112": // 등록
			LocalDate firstHopeDay; // 제1희망일자
			LocalDate secondHopeDay; // 제2희망일자

			try {
				// 제1희망일자 변환
				firstHopeDay = LocalDate.parse(reservVo.getReserv1stHopeDay().substring(0, 8),
						DateTimeFormatter.ofPattern("yyyyMMdd"));
				resultStr = firstHopeDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				resultStr += " ";
				resultStr += reservVo.getReserv1stHopeDay().substring(8); // 오전오후 + 시간대

				if (!ValidUtil.isEmpty(reservVo.getReserv2ndHopeDay())) { // 제2희망일자가 유효할 경우
					// 제2희망일자 변환
					secondHopeDay = LocalDate.parse(reservVo.getReserv2ndHopeDay().substring(0, 8),
							DateTimeFormatter.ofPattern("yyyyMMdd"));

					// 제1희망일자와 결합
					resultStr += " / ";
					resultStr += secondHopeDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					resultStr += " ";
					resultStr += reservVo.getReserv2ndHopeDay().substring(8); // 오전오후 + 시간대
				}
			} catch (Exception e) {
				log.warn("날짜 변환 중 에러", e);
				throw new VitaException("VR000", "날짜 변환 중 에러가 발생하였습니다"); // 내부 에러
			}

			// 변수 변환
			msg = msg.replace("#{희망일시}", resultStr);
			break;

		case "2113": // 확정
		case "2117": // 변경확정
			LocalDate reservDay;
			try {
				reservDay = LocalDate.parse(confirmReservDay.substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"));
				resultStr = reservDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				resultStr += " ";
				resultStr += confirmReservDay.substring(8); // 오전오후 + 시간대
			} catch (Exception e) {
				log.warn("날짜 변환 중 에러", e);
				throw new VitaException("VR000", "날짜 변환 중 에러가 발생하였습니다");
			}
			msg = msg.replace("#{확정일시}", resultStr);
			msg = msg.replace("#{확정일자}", resultStr);
			break;

		case "2116": // 예약실패
			break;
		case "2118": // 예약취소확정
			break;
		default:
			log.warn("등록되지 않은 코드 {}", serviceInfoId);
			throw new VitaException("VR000", "등록된 메세지 코드가 아닙니다(카카오)");
		}

		// 3. 메세지 전송
		ReqMessageRequest req = new ReqMessageRequest();
		req.setMessage(msg);
		req.setTemplateCode(hcServiceInfoVo.getServiceInfoName()); // KMIXXXX
		req.setReceivePhoneNo(reservVo.getMobileNo());
		req.setButtonTitle(""); // 기본형
		req.setMobileUrl(""); // url 이동 없음
		req.setPcUrl(""); // url 이동 없음
		req.setCustomerId(reservVo.getCustomerId());// 고객사ID
		req.setName(reservVo.getRosterName());// 이름

		ReqMessageResponse resp;
		try {
			// Connection.java 추후 수정 필요(API 송신 설정)
			resp = connection.request(req, ReqMessageResponse.class, "alarm/sendmessage", HttpMethod.POST);
		} catch (VitaException e) {
			log.warn("연계실패 to (alarm/sendmessage)", e);
			throw new VitaException(VitaCode.API_ERROR);
		}

		if (!resp.getSuccessYn().equals("Y")) {
			log.warn("메시지 발송 실패. serviceInfoId:{}", hcServiceInfoVo.getServiceInfoId());
			throw new VitaException(VitaCode.KAKAO_MSG_SEND_FAILED);
		}
	}

	public void newChangeCapaAboutPlaceEquipSepecial(String flag, String firstHopeDay, String secondHopeDay,
			String DeferDay, String partnerCenterId, int checkukpRosterNo, List<String> testItemInfoStringList,
			String checkupPlaceId, String deferYn, String callDivision, String groupNo) throws VitaException {

		if (groupNo == null)
			newChangeCapaAboutPlaceEquipSepecial(flag, firstHopeDay, secondHopeDay, DeferDay, partnerCenterId,
					checkukpRosterNo, testItemInfoStringList, checkupPlaceId, deferYn, callDivision);
		else {
			// 장소케파
			changeGroupCapa(flag, firstHopeDay, checkupPlaceId, partnerCenterId, groupNo);
			changeGroupCapa(flag, secondHopeDay, checkupPlaceId, partnerCenterId, groupNo);
			if ("R".equals(callDivision)) {// 예약
				log.info("예약 케파증감");
			} else if ("C".equals(callDivision)) {
				log.info("취소 케파증감");
			} else if ("U".equals(callDivision)) {
				log.info("변경 케파증감");
			} else if ("F".equals(callDivision)) {
				log.info("확정 케파증감");
			}

			// 장비케파
			for (String testItemCd : testItemInfoStringList) { // 검사항목에 대하여
				// 대장내시경 분리검진일 경우 장소케파도 차감
				if ((NORMAL_COLON_TEST_ITEM_CD.equals(testItemCd) // 대장일반이거나
						|| SLEEP_COLON_TEST_ITEM_CD.equals(testItemCd) // 대장수면이라면
						|| NORMAL_COLON.equals(testItemCd) || SLEEP_COLON.equals(testItemCd)) && "Y".equals(deferYn)) {
					changeGroupCapa(flag, DeferDay, checkupPlaceId, partnerCenterId, groupNo);
				}
				newChangeGroupEquipCapaByEquipNo(flag, testItemCd, partnerCenterId, firstHopeDay, secondHopeDay,
						DeferDay, deferYn, callDivision, groupNo);
			}

			// 특검케파
			changeSpecialCapa(flag, firstHopeDay, partnerCenterId, checkukpRosterNo);
			changeSpecialCapa(flag, secondHopeDay, partnerCenterId, checkukpRosterNo);
		}

	}

	public void newChangeGroupEquipCapaByEquipNo(String flag, String testItemCd, String partnerCenterId,
			String firstHopeDay, String secondHopeDay, String deferDay, String deferYn, String callDivision,
			String groupNo) throws VitaException {
		log.debug(
				"flag: {} / testItemCd: {} / partnerCenterId: {} / firstHopeDay: {} / secondHopeDay: {} / deferDay: {} / deferYn: {}",
				flag, testItemCd, partnerCenterId, firstHopeDay, secondHopeDay, deferDay, deferYn);

		// 장비번호 조회
		GetEquipNoDto getEquipNoVo = new GetEquipNoDto();
		getEquipNoVo.setTestItemCd(testItemCd);
		getEquipNoVo.setPartnerCenterId(partnerCenterId);

		List<Integer> equipNoList = mapper.selectEquipNo(getEquipNoVo);

		for (Integer equipNo : equipNoList) {
			if ("Y".equals(deferYn)) {// 분리검진일 경우
				if (NORMAL_COLON_TEST_ITEM_CD.equals(testItemCd) // 대장일반이거나
						|| SLEEP_COLON_TEST_ITEM_CD.equals(testItemCd) // 대장수면이라면
						|| NORMAL_COLON.equals(testItemCd) || SLEEP_COLON.equals(testItemCd)) {
					// 분리검진날짜에 케파 증감(대장내시경)
					changeGroupEquipCapa(flag, deferDay, equipNo, partnerCenterId, groupNo);
				} else {
					// 대장내시경제외 장비케파 1,2차 희망일 증감
					changeGroupEquipCapa(flag, firstHopeDay, equipNo, partnerCenterId, groupNo);
					changeGroupEquipCapa(flag, secondHopeDay, equipNo, partnerCenterId, groupNo);
				}
			} else { // 분리검진이 아니라면 장비케파 1,2차 희망일 증감
				changeGroupEquipCapa(flag, firstHopeDay, equipNo, partnerCenterId, groupNo);
				changeGroupEquipCapa(flag, secondHopeDay, equipNo, partnerCenterId, groupNo);
			}
		}

	}

	// 그룹 장비 케파 증감
	public void changeGroupEquipCapa(String flag, String capaDayTime, int testEquipNo, String partnerCenterId,
			String groupNo) throws VitaException {

		if (ValidUtil.isEmpty(flag) || ValidUtil.isEmpty(capaDayTime) || testEquipNo == 0
				|| ValidUtil.isEmpty(partnerCenterId) || ValidUtil.isEmpty(groupNo)) {
			log.error("장비케파 증감 필수값 누락. 증감여부:{}, capaDayTime:{}, testEquipNo:{}, groupNo:{}", flag, capaDayTime,
					testEquipNo, groupNo);
			return;
		}

		Map<String, String> map = new HashMap<>();

		map.put("PARTNER_CENTER_ID", partnerCenterId);
		map.put("CAPA_DAY", capaDayTime.substring(0, 8));
		map.put("CAPA_TIME", capaDayTime.substring(8));
		map.put("TEST_EQUIP_NO", String.valueOf(testEquipNo));
		map.put("GROUP_NO", groupNo);

		if (flag.equals("plus")) {
			try {
				mapper.updatePlusGroupEquipCapa(map);
			} catch (Exception e) {
				log.warn("그룹 장비케파 증가 중 에러. map:{}", map, e);
			}
		} else if (flag.equals("minus")) {
			try {
				mapper.updateMinusGroupEquipCapa(map);
			} catch (Exception e) {
				log.warn("그룹 장비케파 감소 중 에러. map:{}", map, e);
			}
		}
	}

	/**
	 * 그룹 케파 증감처리
	 * 
	 * @param flag
	 * @param capaDayTime
	 * @param checkupPlaceId
	 * @param partnerCenterId
	 * @param groupNo
	 * @throws VitaException
	 */
	public void changeGroupCapa(String flag, String capaDayTime, String checkupPlaceId, String partnerCenterId,
			String groupNo) throws VitaException {

		if (ValidUtil.isEmpty(flag) || ValidUtil.isEmpty(capaDayTime) || ValidUtil.isEmpty(checkupPlaceId)
				|| ValidUtil.isEmpty(partnerCenterId) || ValidUtil.isEmpty(groupNo)) {
			log.error("그룹 케파 증감 필수값 누락. 증감여부:{}, capaDayTime:{}, checkupPlaceId:{}, groupNo:{}", flag, capaDayTime,
					checkupPlaceId, groupNo);
			return;
		}

		Map<String, String> map = new HashMap<>();

		map.put("PARTNER_CENTER_ID", partnerCenterId);
		map.put("CAPA_DAY", capaDayTime.substring(0, 8));
		map.put("CAPA_TIME", capaDayTime.substring(8));
		map.put("CHECKUP_PLACE_ID", checkupPlaceId);
		map.put("GROUP_NO", groupNo);

		if (flag.equals("plus")) {
			try {
				mapper.updatePlusGroupCapa(map);
			} catch (Exception e) {
				log.warn("그룹 케파 증가 중 에러. map:{}", map, e);
			}
		} else if (flag.equals("minus")) {
			try {
				mapper.updateMinusGroupCapa(map);
			} catch (Exception e) {
				log.warn("그룹 케파 감소 중 에러. map:{}", map, e);
			}
		}
	}

	/**
	 * 장비/장소 케파 갱신
	 * 
	 * @param flag
	 * @param firstHopeDay
	 * @param secondHopeDay
	 * @param DeferDay
	 * @param partnerCenterId
	 * @param checkukpRosterNo
	 * @param testItemInfoStringList
	 * @param checkupPlaceId
	 * @param deferYn
	 * @param callDivision
	 * @throws VitaException
	 */
	public void newChangeCapaAboutPlaceEquipSepecial(String flag, String firstHopeDay, String secondHopeDay,
			String DeferDay, String partnerCenterId, int checkukpRosterNo, List<String> testItemInfoStringList,
			String checkupPlaceId, String deferYn, String callDivision) throws VitaException {

		// 장소케파
		changeCapa(flag, firstHopeDay, checkupPlaceId, partnerCenterId);
		changeCapa(flag, secondHopeDay, checkupPlaceId, partnerCenterId);
		if ("R".equals(callDivision)) {// 예약
			log.info("예약 케파증감");
		} else if ("C".equals(callDivision)) {
			log.info("취소 케파증감");
		} else if ("U".equals(callDivision)) {
			log.info("변경 케파증감");
		}

		// 장비케파
		for (String testItemCd : testItemInfoStringList) { // 검사항목에 대하여
			// 대장내시경 분리검진일 경우 장소케파도 차감
			if ((NORMAL_COLON_TEST_ITEM_CD.equals(testItemCd) // 대장일반이거나
					|| SLEEP_COLON_TEST_ITEM_CD.equals(testItemCd) // 대장수면이라면
					|| NORMAL_COLON.equals(testItemCd) || SLEEP_COLON.equals(testItemCd)) && "Y".equals(deferYn)) {
				changeCapa(flag, DeferDay, checkupPlaceId, partnerCenterId);
			}
			newChangeEquipCapaByEquipNo(flag, testItemCd, partnerCenterId, firstHopeDay, secondHopeDay, DeferDay,
					deferYn, callDivision);
		}

		// 특검케파
		changeSpecialCapa(flag, firstHopeDay, partnerCenterId, checkukpRosterNo);
		changeSpecialCapa(flag, secondHopeDay, partnerCenterId, checkukpRosterNo);

	}

	/**
	 * 특검 케파 증감처리(HC_CENTER_SPECIAL_CAPA)
	 * 
	 * @param flag
	 * @param capaDayTime
	 * @param partnerCenterId
	 * @param rosterNo
	 * @throws VitaException
	 */
	public void changeSpecialCapa(String flag, String capaDayTime, String partnerCenterId, int rosterNo)
			throws VitaException {

		if (ValidUtil.isEmpty(flag) || ValidUtil.isEmpty(capaDayTime) || ValidUtil.isEmpty(partnerCenterId)) {
			log.error("특검케파 증감 필수값 누락. 증감여부:{}, capaDayTime:{}", flag, capaDayTime);
			return;
		}

		String specialCheckupYn = mapper.selectSpecialRoster(rosterNo);
		if (ValidUtil.isEmpty(specialCheckupYn)) {
			specialCheckupYn = "N";
		}
		if (specialCheckupYn.equals("Y")) {
			GetRemainCapaDto capaVo = new GetRemainCapaDto();
			capaVo.setPartnerCenterId(partnerCenterId);
			capaVo.setCapaDay(capaDayTime.substring(0, 8)); // yyyyMMdd
			capaVo.setCapaTime(capaDayTime.substring(8)); // AM or PM

			if (flag.equals("plus")) {
				try {
					mapper.updatePlusSpecialCapa(capaVo);
				} catch (Exception e) {
					log.warn("특검케파 증가 중 에러. vo:{}", capaVo, e);
				}
			} else if (flag.equals("minus")) {
				try {
					mapper.updateMinusSpecialCapa(capaVo);
				} catch (Exception e) {
					log.warn("특검케파 감소 중 에러. vo:{}", capaVo, e);
				}
			}
		}

	}

	/**
	 * 장비 케파 갱신(changeEquipCapa 호출)
	 * 
	 * @param flag
	 * @param testItemCd
	 * @param partnerCenterId
	 * @param firstHopeDay
	 * @param secondHopeDay
	 * @param deferDay
	 * @param deferYn
	 * @param callDivision
	 * @throws VitaException
	 */
	public void newChangeEquipCapaByEquipNo(String flag, String testItemCd, String partnerCenterId, String firstHopeDay,
			String secondHopeDay, String deferDay, String deferYn, String callDivision) throws VitaException {
		log.debug(
				"flag: {} / testItemCd: {} / partnerCenterId: {} / firstHopeDay: {} / secondHopeDay: {} / deferDay: {} / deferYn: {}",
				flag, testItemCd, partnerCenterId, firstHopeDay, secondHopeDay, deferDay, deferYn, callDivision);

		// 장비번호 조회
		GetEquipNoDto getEquipNoVo = new GetEquipNoDto();
		getEquipNoVo.setTestItemCd(testItemCd);
		getEquipNoVo.setPartnerCenterId(partnerCenterId);

		List<Integer> equipNoList = mapper.selectEquipNo(getEquipNoVo);

		for (Integer equipNo : equipNoList) {
			if ("Y".equals(deferYn)) {// 분리검진일 경우
				if (NORMAL_COLON_TEST_ITEM_CD.equals(testItemCd) // 대장일반이거나
						|| SLEEP_COLON_TEST_ITEM_CD.equals(testItemCd) // 대장수면이라면
						|| NORMAL_COLON.equals(testItemCd) || SLEEP_COLON.equals(testItemCd)) {
					// 분리검진날짜에 케파 증감(대장내시경)
					changeEquipCapa(flag, deferDay, equipNo, partnerCenterId);
				} else {
					// 대장내시경제외 장비케파 1,2차 희망일 증감
					changeEquipCapa(flag, firstHopeDay, equipNo, partnerCenterId);
					changeEquipCapa(flag, secondHopeDay, equipNo, partnerCenterId);
				}
			} else { // 분리검진이 아니라면 장비케파 1,2차 희망일 증감
				changeEquipCapa(flag, firstHopeDay, equipNo, partnerCenterId);
				changeEquipCapa(flag, secondHopeDay, equipNo, partnerCenterId);
			}
		}

	}

	/**
	 * 케파 증감 처리(HC_CENTER_PLACE_CAPA)
	 * 
	 * @param flag
	 * @param capaDayTime
	 * @param checkupPlaceId
	 * @param partnerCenterId
	 * @throws VitaException
	 */
	public void changeCapa(String flag, String capaDayTime, String checkupPlaceId, String partnerCenterId)
			throws VitaException {

		if (ValidUtil.isEmpty(flag) || ValidUtil.isEmpty(capaDayTime) || ValidUtil.isEmpty(checkupPlaceId)
				|| ValidUtil.isEmpty(partnerCenterId)) {
			log.error("케파 증감 필수값 누락. 증감여부:{}, capaDayTime:{}, checkupPlaceId:{}", flag, capaDayTime, checkupPlaceId);
			return;
		}

		GetRemainCapaDto capaVo = new GetRemainCapaDto();
		capaVo.setPartnerCenterId(partnerCenterId);
		capaVo.setCapaDay(capaDayTime.substring(0, 8)); // yyyyMMdd
		capaVo.setCapaTime(capaDayTime.substring(8)); // AM or PM or .... 1130
		capaVo.setCheckupPlaceId(checkupPlaceId);

		if (flag.equals("plus")) {
			try {
				mapper.updatePlusCapa(capaVo);
			} catch (Exception e) {
				log.warn("케파 증가 중 에러. vo:{}", capaVo, e);
			}
		} else if (flag.equals("minus")) {
			try {
				mapper.updateMinusCapa(capaVo);
			} catch (Exception e) {
				log.warn("케파 감소 중 에러. vo:{}", capaVo, e);
			}
		}
	}

	/**
	 * 장비 케파 증감 처리(HC_CENTER_EQUIP_CAPA)
	 * 
	 * @param flag
	 * @param capaDayTime
	 * @param testEquipNo
	 * @param partnerCenterId
	 * @throws VitaException
	 */
	public void changeEquipCapa(String flag, String capaDayTime, int testEquipNo, String partnerCenterId)
			throws VitaException {

		if (ValidUtil.isEmpty(flag) || ValidUtil.isEmpty(capaDayTime) || testEquipNo == 0
				|| ValidUtil.isEmpty(partnerCenterId)) {
			log.error("장비케파 증감 필수값 누락. 증감여부:{}, capaDayTime:{}, testEquipNo:{}", flag, capaDayTime, testEquipNo);
			return;
		}

		GetRemainCapaDto capaVo = new GetRemainCapaDto();
		capaVo.setPartnerCenterId(partnerCenterId);
		capaVo.setCapaDay(capaDayTime.substring(0, 8)); // yyyyMMdd
		capaVo.setCapaTime(capaDayTime.substring(8)); // AM or PM
		capaVo.setTestEquipNo(testEquipNo);

		if (flag.equals("plus")) {
			try {
				mapper.updatePlusEquipCapa(capaVo);
			} catch (Exception e) {
				log.warn("장비케파 증가 중 에러. vo:{}", capaVo, e);
			}
		} else if (flag.equals("minus")) {
			try {
				mapper.updateMinusEquipCapa(capaVo);
			} catch (Exception e) {
				log.warn("장비케파 감소 중 에러. vo:{}", capaVo, e);
			}
		}
	}

	/**
	 * 그룹 케파넘버 조회
	 * 
	 * @param partnerCenterId
	 * @param customerId
	 * @return
	 */
	public String getGroupCapaNo(String partnerCenterId, String customerId) {

		Map<String, String> map = new HashMap<>();

		map.put("partnerCenterId", partnerCenterId);
		map.put("customerId", customerId);

		return mapper.selectGroupCapaNo(map);
	}

	/**
	 * 검사항목코드 조회(HC_TEST_ITEM_CD)
	 * 
	 * @param partnerCenterId
	 * @param nowCheckupItemCd
	 * @param reservNo
	 * @param checkupProductNo
	 * @param nowChoiceGroupNo
	 * @return
	 * @throws VitaException
	 */
	public List<GetTestItemCdDto> getTestItemCd(String partnerCenterId, String nowCheckupItemCd, int reservNo,
			int checkupProductNo, int nowChoiceGroupNo) throws VitaException {

		/* 3-1. 검사항목코드 조회 */
		GetTestItemCdDto getTestItemCdVo = new GetTestItemCdDto();
		getTestItemCdVo.setPartnerCenterId(partnerCenterId);
		getTestItemCdVo.setCheckupItemCd(nowCheckupItemCd);
		getTestItemCdVo.setCheckupProductNo(checkupProductNo);
		getTestItemCdVo.setChoiceGroupNo(nowChoiceGroupNo);

		List<GetTestItemCdDto> resultTestItemCdList;
		try {
			resultTestItemCdList = mapper.selectTestItemCd(getTestItemCdVo);
		} catch (Exception e) {
			log.error("DB ERROR (getTestItemCd) getTestItemCdVo : {}", getTestItemCdVo, e);
			throw new VitaException(VitaCode.DATABASE_ERROR);
		}

		if (ValidUtil.isEmpty(resultTestItemCdList)) {
			log.error("추가/선택 검진항목코드가 검사항목과 맵핑이 되지 않음 (getTestItemCd) nowCheckupItemCd : {}", nowCheckupItemCd);
			throw new VitaException("VR000", "추가/선택 검진항목코드가 검사항목과 맵핑이 되지 않습니다.");
		}

		return resultTestItemCdList;
	}

	/**
	 * 연기검진 항목 등록
	 * 
	 * @param request
	 * @return
	 * @throws VitaException
	 */
	public int registDeferredCheckup(ReservRequest request) throws VitaException {
		if (!ValidUtil.isEmpty(request.getDeferReservHopeDay())) {
			if (request.getPartnerCenterId().startsWith("H0000")) {
				// KMI
				/* 연기예약 가능한 항목 조회 */
				List<String> deferCheckupTestItems = new ArrayList<>();
				deferCheckupTestItems = mapper.selectDeferCheckupItem();
				log.debug("연기예약 가능한 항목 조회 : " + deferCheckupTestItems);

				/* 유효한 연기검사항목 등록 */
				List<String> checkupItemList = new ArrayList<>();
				for (ReservRequest.CheckupItemList checkupItemCd : request.getCheckupItemList()) {
					checkupItemList.add(checkupItemCd.getCheckupItemCd());
				}
				log.debug("검사항목 리스트 : " + checkupItemList);
				String commonItems = deferCheckupTestItems.stream().filter(checkupItemList::contains)
						.collect(Collectors.joining());
				if (commonItems.isEmpty()) {
					throw new VitaException("ER0002", "일치하는 연기검사항목이 없습니다.");
				}
				log.debug("연기검사항목 : " + commonItems);
				int choiceGroupNo = 0;
				String itemDivCd = "";
				for (int i = 0; i < request.getCheckupItemList().size(); i++) {
					if (commonItems.equals(request.getCheckupItemList().get(i).getCheckupItemCd())) {
						choiceGroupNo = request.getCheckupItemList().get(i).getChoiceGroupNo();
						itemDivCd = request.getCheckupItemList().get(i).getItemDivCd();
					}
				}
				RegDeferReservValIdDto regDeferReservVal = new RegDeferReservValIdDto();
				regDeferReservVal.setReservNo(request.getReservNo());
				String testItemCd = mapper.selectTestItemCd(commonItems.toString(), request.getPartnerCenterId());
				regDeferReservVal.setTestItemCd(testItemCd);
				regDeferReservVal.setChoiceGroupNo(choiceGroupNo);
				mapper.insertDeferValIdCheckupTestItem(regDeferReservVal);

				DeferCheckupReservDto deferReservVo = new DeferCheckupReservDto();
				deferReservVo.setReservNo(request.getReservNo());
				if (request.getOnpayAmount() != 0) { // 온라인결제라면
					deferReservVo.setReservSt("05"); // 결제대기
				} else { // 온라인결제가 아니라면
					deferReservVo.setReservSt("50"); // 예약등록
				}
				deferReservVo.setReserv1stHopeDay(request.getDeferReservHopeDay());
				deferReservVo.setLastModifier(request.getLastModifier());
				deferReservVo.setLastPath(request.getLastPath());
				deferReservVo.setPartnerCenterId(request.getPartnerCenterId());

				try {
					mapper.insertDefferReserv(deferReservVo); // HC_RESERV
				} catch (Exception e) {
					log.warn("DB error (regDefferReserv)", e);
					throw new VitaException(VitaCode.DATABASE_ERROR); // DB error
				}

				/* 연기예약 항목 등록 */
				DeferReservTestItemDto deferReservTestItemVo = new DeferReservTestItemDto();
				deferReservTestItemVo.setDeferCheckupReservNo(deferReservVo.getDeferCheckupReservNo());
				deferReservTestItemVo.setReservNo(request.getReservNo());
				deferReservTestItemVo.setTestItemCd(testItemCd);
				deferReservTestItemVo.setCheckupItemCd(commonItems.toString());
				deferReservTestItemVo.setChoiceGroupNo(choiceGroupNo);
				deferReservTestItemVo.setAddCheckupYn("N");
				deferReservTestItemVo.setItemDivCd(itemDivCd);
				deferReservTestItemVo.setDeferReservMomentAmount(0);
				mapper.insertDeferReservTestItem(deferReservTestItemVo);

				/* 유효한 연기검사항목 상태 사용으로 업데이트 */
				mapper.updateDeferValidCheckupTestItem(deferReservTestItemVo);
				request.setDeferItem(testItemCd);

				return deferReservVo.getDeferCheckupReservNo();
			} else {
				// deferReservNo = service.regDeferReserv(request);
				// 외 협력병원
				// vo setting
				DeferCheckupReservDto deferReservVo = new DeferCheckupReservDto();
				deferReservVo.setReservNo(request.getReservNo());
				deferReservVo.setReservSt("10"); // 예약등록

				if (request.getOnpayAmount() != 0) { // 온라인결제라면
					deferReservVo.setReservSt("05"); // 결제대기
				} else if (request.getOnpayAmount() == 0) { // 온라인결제가 아니라면
					deferReservVo.setReservSt("10"); // 예약등록
				}

				deferReservVo.setReserv1stHopeDay(request.getDeferReservHopeDay());
				deferReservVo.setLastModifier(request.getLastModifier());
				deferReservVo.setLastPath(request.getLastPath());

				// db insert
				try {
					mapper.insertDefferReserv(deferReservVo); // HC_RESERV
				} catch (Exception e) {
					log.warn("DB error (regDefferReserv)", e);
					throw new VitaException(VitaCode.DATABASE_ERROR); // DB error
				}
				return deferReservVo.getDeferCheckupReservNo();
			}
		} else {
			return 0;
		}
	}

	/**
	 * 회사지원금액(가족) 업데이트
	 * 
	 * @throws VitaException
	 */
	public void updateCompanySupportAmt(int rosterNo, ReservRequest request) {
		// 회사지원 사용금액 업데이트(HC_CHECKUP_ROSTER.COMPANY_SUPPORT_USAGE_AMOUNT)
		UpdtCompanySupAmtDto vo = mapper.selectCompanySup(rosterNo); // 대상자 모든 지원금액 정보 포함

		log.debug("대상자 정보: {}", vo);
		int chargeAmount = vo.getSuperRosterCompanySupportChargeAmt(); // 충전금액
		int usageAmount = vo.getSuperRosterCompanySupportUsageAmt(); // 사용금액
		if (vo.getEmployRelationType().equals("00")) { // 본인이 아닐경우
			chargeAmount = vo.getCompanySupportChargeAmt(); // 임직원의 충전금액
			usageAmount = vo.getCompanySupportUsageAmt(); // 임직원의 사용금액
		}
		int usedSumCompanySupportAmt = 0;
		log.debug("rosterNo:{}, calAmountResponse:{}, vo:{}", rosterNo, vo);

		int originAmount = usageAmount;
		int originRosterNo = 0;
		int finalReservCompanySupportAmount = 0;

		/*
		 * 10(개별비용지원) : 각 대상자에게 회사지원금액만큼 검진 비용을 지원 20(직원제외 합산) : 회사지원금액에서 직원의 검진지원금액을
		 * 제외하고 각 대상자의 검진 비용의 합산 금액 중 회사지원금액 만큼 회사에서 지원 30(직원포함 합산) : 회사지원금액에서 직원의
		 * 검진지원금액을 포함하여 각 대상자의 검진 비용의 합산 금액 중 회사지원금액 만큼 회사에서 지원 90(사용안함)
		 */
		// 관리자가 추가한 임직원만 해당한다.
		if (vo.getEmployRelationType().equals("00") // 대상자관계타입이 00(본인)이고
				&& vo.getMgmtType().equals("10")) { // 대상자관리타입이 10(어드민추가)라면

			if (vo.getFamilySupportType().equals("30") // 가족지원방식이 30(직원포함합산)이고
					&& vo.getCompanySupportAmt() == 0) { // 이전 추가 대상자 제외

				// 회사지원금액 = 회사지원사용금액 + 회사지원금액
				usedSumCompanySupportAmt = usageAmount + request.getCompanySupportAmount();
				log.debug("기존사용회사지원금액: {}", usageAmount);
				log.debug("해당사용회사지원금액: {}", request.getCompanySupportAmount());

				if (usedSumCompanySupportAmt < chargeAmount) { // 회사지원금액 < (충전금액 - 사용금액)
					finalReservCompanySupportAmount = usedSumCompanySupportAmt; // 최종사용금액 = 회사지원금액
				} else { // 회사지원금액 >= (충전금액 - 사용금액)
					usedSumCompanySupportAmt = chargeAmount; // 회사지원금액 = 충전금액(사용완료)
					finalReservCompanySupportAmount = usedSumCompanySupportAmt; // 최종사용금액 = 충전금액 - 사용금액
				}
				// 회사지원사용금액 업데이트
				vo.setRosterNo(rosterNo);
				vo.setCompanySupportAmt(finalReservCompanySupportAmount);
				// mapper.updtCompanySupportUsageAmount(vo);
				originRosterNo = rosterNo;
			}
		} else if (
		// mgmt타입이 20이면 가짜임직원이니까
		(vo.getFamilySupportType().equals("20") // 가족지원방식이 20(개별비용지원)이거나
				|| vo.getFamilySupportType().equals("30")) // 가족지원방식이 30(직원포함합산)이거나
				&& vo.getCompanySupportAmt() == 0 // 이전 대상자 제외
				&& (vo.getMgmtType().equals("20") // 대상자관리타입이 20(PC,MOBILE추가) 이거나
						|| "C".equals(vo.getRegedPath()))) { // 명동콜센터에서 등록된 대상자면 <- 2024/05/07 추가

			// 회사지원금액 = 회사지원사용금액 + 회사지원금액
			usedSumCompanySupportAmt = usageAmount + request.getCompanySupportAmount();
			log.debug("기존사용회사지원금액: {}", usageAmount);
			log.debug("해당사용회사지원금액: {}", request.getCompanySupportAmount());

			if (usedSumCompanySupportAmt < chargeAmount) { // 회사지원금액 < (충전금액 - 사용금액)
				finalReservCompanySupportAmount = usedSumCompanySupportAmt; // 최종사용금액 = 회사지원금액
			} else { // 회사지원금액 >= (충전금액 - 사용금액)
				usedSumCompanySupportAmt = chargeAmount; // 회사지원금액 = 충전금액(사용완료)
				finalReservCompanySupportAmount = usedSumCompanySupportAmt; // 최종사용금액 = 충전금액 - 사용금액
			}
			// 회사지원사용금액 업데이트
			vo.setCompanySupportAmt(finalReservCompanySupportAmount);
			vo.setRosterNo(vo.getSuperRosterNo());
			// mapper.updtCompanySupportUsageAmount(vo);
			originRosterNo = vo.getSuperRosterNo();
		}

		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("originAmount", originAmount);
		result.put("originRosterNo", originRosterNo);
		result.put("finalReservCompanySupportAmount", finalReservCompanySupportAmount);
		globalOriginAmount = originAmount;
		globalOriginRosterNo = originRosterNo;
		globalFinalReservCompanySupportAmount = finalReservCompanySupportAmount;
	}

	/**
	 * 예약 등록 VO Setting
	 * 
	 * @param request
	 * @param hcCheckupRosterResponse
	 * @param checkupProductVo
	 * @return
	 * @throws VitaException
	 */
	public RegReservDto makeRegReservVo(ReservRequest request, CheckupRosterDto hcCheckupRosterResponse,
			CheckupProductDto checkupProductVo) throws VitaException {

		// vo setting
		RegReservDto regReservVo = new RegReservDto();
		regReservVo.setCheckupProductNo(request.getCheckupProductNo());
		regReservVo.setCheckupRosterNo(hcCheckupRosterResponse.getCheckupRosterNo());
		regReservVo.setPartnerCenterId(request.getPartnerCenterId());
		regReservVo.setCheckupDivCd(request.getCheckupDivCd());
		regReservVo.setSuperReservNo(0);
		regReservVo.setNote(request.getNote());
		regReservVo.setReserv1stHopeDay(request.getReserv1stHopeDay());
		regReservVo.setReserv2ndHopeDay(request.getReserv2ndHopeDay());
		regReservVo.setRequestContents(request.getRequestContents());
		regReservVo.setHcAgreeYn(request.getHcAgreeYn());
		regReservVo.setResultReceiveWayCd(request.getResultReceiveWayCd());
		regReservVo.setForceReservYn(request.getForceReservYn());
		regReservVo.setSelfPayAmount(request.getSelfPayAmount());
		regReservVo.setCompanySupportAmount(request.getCompanySupportAmount());
		regReservVo.setOnpayAmount(request.getOnpayAmount());
		regReservVo.setOffpayExpectAmount(request.getOffpayExpectAmount());
		regReservVo.setAgreeTermsList(request.getUserTermsAgreeList());
		regReservVo.setBatchReservYn(request.getBatchReservYN());
		regReservVo.setSmsYn(request.getSmsYN());
		regReservVo.setLastModifier(request.getLastModifier());
		regReservVo.setLastPath(request.getLastPath());
		regReservVo.setVipYn(hcCheckupRosterResponse.getVipYn());
		regReservVo.setCustomAmountYn(request.getCustomAmountYN());
		regReservVo.setCustomTotalAmount(request.getCustomTotalAmount());
		regReservVo.setCustomCompanyAmount(request.getCustomCompanyAmount());
		regReservVo.setCustomSelfAmount(request.getCustomSelfAmount());

		regReservVo.setPregnancyYn(request.getPregnancyYN());
		regReservVo.setPossiblePregnancyYn(request.getPossiblePregnancyYN());
		regReservVo.setFeedingYn(request.getFeedingYN());
		regReservVo.setMensesYn(request.getMensesYN());
		regReservVo.setAnticoagulantYn(request.getAnticoagulantYN());
		regReservVo.setMelituriaYn(request.getMelituriaYN());
		regReservVo.setArteriotonyYn(request.getArteriotonyYN());
		regReservVo.setMedicationText(request.getMedicationText());
		regReservVo.setNephropathyYn(request.getNephropathyYN());
		regReservVo.setCardiopathyYn(request.getCardiopathyYN());
		regReservVo.setRenalFailureYn(request.getRenalFailureYN());
		regReservVo.setPeritonealDialysisYn(request.getPeritionealDialysisYN());
		regReservVo.setHemodialysisYn(request.getHemodialysisYN());
		regReservVo.setCaseHistoryText(request.getCaseHistoryText());

		if (request.getLastPath().equals("P") || request.getLastPath().equals("M")) {
			regReservVo.setVipYn(hcCheckupRosterResponse.getVipYn());
		} else {
			regReservVo.setVipYn(request.getVipYN());
		}
		regReservVo.setTransCustomerYn(request.getTransCustomerYN());
		regReservVo.setPreAcceptYn(request.getPreAcceptYN());

		String placeId = "";
		if (!ValidUtil.isEmpty(checkupProductVo.getCenterPlaceId())) { // 상품의 검진장소가 유효하다면
			placeId = checkupProductVo.getCenterPlaceId(); // 검진장소 설정
		} else { // 상품 검진장소가 없다면
			// 금액대별 검진장소 조회
			CheckupPlaceDto checkupPlaceVo = new CheckupPlaceDto();
			checkupPlaceVo.setCheckupProductNo(request.getCheckupProductNo());
			checkupPlaceVo.setPartnerCenterId(request.getPartnerCenterId());
			try {
				checkupPlaceVo = mapper.selectCheckupPlace(checkupPlaceVo); // HC_CENTER_PLACE
			} catch (Exception e) {
				log.error("DB error (getSedationAmt)", e);
				throw new VitaException(VitaCode.DATABASE_ERROR); // DB error
			}
			if (ValidUtil.isEmpty(checkupPlaceVo)) {
				log.error("해당하는 검진장소가 없습니다!");
				throw new VitaException(VitaCode.CHECKUP_PLACE_NOT_FOUND); // 처리할 수 없습니다.
			}

			placeId = checkupPlaceVo.getCenterPlaceId();
		}

		regReservVo.setCheckupPlaceId(placeId);
		regReservVo.setProductIndex(checkupProductVo.getProductIndex());

		regReservVo.setProductPrice(checkupProductVo.getSettlePrice()); // 예약 당시 상품 금액
		regReservVo.setSelfpaySedationAmt(request.getSelfPaySedationAmt());
		regReservVo.setCompanySupportSedationAmt(request.getCompanySupportSedationAmt());

		regReservVo.setZipCd(request.getZipCd());
		regReservVo.setAddress(request.getAddress());
		regReservVo.setAddressDetail(request.getAddressDetail());

		regReservVo.setSuppliesZipCd(request.getSuppliesZipCd());
		regReservVo.setSuppliesAddress(request.getSuppliesAddress());
		regReservVo.setSuppliesAddressDetail(request.getSuppliesAddressDetail());

		// 대상자에 입력된 하위 정보는 예약시 인입된 값과 다를 수 있다.
		String phoneNo = !ValidUtil.isEmpty(request.getPhoneNo()) ? request.getPhoneNo()
				: hcCheckupRosterResponse.getPhoneNo();
		regReservVo.setPhoneNo(phoneNo);
		String mobileNo = !ValidUtil.isEmpty(request.getMobileNo()) ? request.getMobileNo()
				: hcCheckupRosterResponse.getMobileNo();
		regReservVo.setMobileNo(mobileNo);
		String email = !ValidUtil.isEmpty(request.getEmail()) ? request.getEmail() : hcCheckupRosterResponse.getEmail();
		regReservVo.setEmail(email);

		// PC / MOBILE 결제 완료 후 예약 등록으로 변경 처리 (20241015 이정호)
		if (request.getAdminReqYn().equals("Y")) {
			if (request.getOnpayAmount() != 0) {
				regReservVo.setReservSt("05"); // 결제대기
			} else if (request.getOnpayAmount() == 0) { // 온라인결제가 아니라면
				regReservVo.setReservSt("90"); // 예약등록
			}
		} else {
			regReservVo.setReservSt("90"); // 예약등록
		}

		regReservVo.setGenderCd(hcCheckupRosterResponse.getGenderCd());
		regReservVo.setCustomerId(hcCheckupRosterResponse.getCustomerId());
		regReservVo.setRosterName(hcCheckupRosterResponse.getRosterName());
		regReservVo.setBirthDay(hcCheckupRosterResponse.getBirthday());
		regReservVo.setPolicyYear(hcCheckupRosterResponse.getPolicyYear());
		regReservVo.setCustomerName(hcCheckupRosterResponse.getCustomerName());
		regReservVo.setReservRequestContents(request.getReservRequestContents());
		regReservVo.setEmployRelationType(hcCheckupRosterResponse.getEmployRelationType());

		regReservVo.setCheckupProductTitle(checkupProductVo.getCheckupProductTitle());
		regReservVo.setCenterProductNo(checkupProductVo.getCenterProductNo());

		regReservVo.setSettlePrice(checkupProductVo.getSettlePrice());
		regReservVo.setMigYn(checkupProductVo.getMigYn());

		return regReservVo;
	}

}
