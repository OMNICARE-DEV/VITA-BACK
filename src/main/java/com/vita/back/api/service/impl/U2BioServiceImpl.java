package com.vita.back.api.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vita.back.api.model.data.ExtIntegrationDto;
import com.vita.back.api.model.data.ReserveDataDto;
import com.vita.back.api.model.data.U2bioPatientDto;
import com.vita.back.api.model.data.U2bioReservAvailableTimeDto;
import com.vita.back.api.model.request.ExchangeRequest;
import com.vita.back.api.model.request.U2bioReservAvailableDateRequest;
import com.vita.back.api.model.response.U2bioPatientResponse;
import com.vita.back.api.model.response.U2bioReservAvailableDateResponse;
import com.vita.back.api.service.U2BioService;
import com.vita.back.common.exception.VitaException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = { Exception.class, VitaException.class })
public class U2BioServiceImpl implements U2BioService {
	private ExtIntegrationDto commonExtInfo;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Value("${url.connecter}")
    private String connectUrl;
    @Value("${url.connecterPort}")
    private String connectPort;
    public U2BioServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }
    
    public void setCommonExtInfo(ExtIntegrationDto commonExtInfo) {
        this.commonExtInfo = commonExtInfo;
    }
    
    /**
     * 개별검사 항목 및 일반검진세트 조회
     * @return
     */
//    public LibCheckupTestRo searchCheckupTest(LibCheckupTestPo params) throws Exception {
//        String url = "/library/checkupTest";
//        String jsonParams = objectMapper.writeValueAsString(params);
//        return sendExchangeRequest(url, HttpMethod.POST, jsonParams, LibCheckupTestRo.class);
//    }
    
    /**
     * 상품 정보 생성/UPDATE
     * @param libProfile
     * @return
     * @throws Exception
     */
//    public String setCheckupItemCode(LibProfile libProfile) throws Exception {
//        //종검세트 생성
//        String jsonParams = objectMapper.writeValueAsString(libProfile);
//        ApiReturnVo res = sendExchangeRequest("/library/profile", HttpMethod.POST, jsonParams, ApiReturnVo.class);
//
//        if(StringUtils.equals(res.getResultCode(), "100") && !StringUtils.isBlank(res.getResultMsg())) {
//            return res.getResultMsg();
//        } else {
//            throw new Exception("Error : 유투바이오 종검 세트 생성 실패");
//        }
//    }
    
    /**
     * 추가검사 단일 생성/update
     * @param libProfile
     * @return
     * @throws Exception
     */
//    public String setAdditionItemCode(LibProfile libProfile) throws Exception {
//        //종검세트 생성
//        String jsonParams = objectMapper.writeValueAsString(libProfile);
//        LibProfile res = sendExchangeRequest("/library/additionTest", HttpMethod.POST, jsonParams, LibProfile.class);
//
//        String profileCode = res.getProfileCode();
//
//        log.debug("{profileCode}" + profileCode);
//        return profileCode;
//    }
    
    /**
     * 유투바이오 추가검사 다건 생성/update
     * @param libAdditionTestPo
     * @throws Exception
     */
//    public void setAdditionItemCodeList(LibAdditionTestPo libAdditionTestPo) throws Exception {
//        //종검세트 생성
//        String jsonParams = objectMapper.writeValueAsString(libAdditionTestPo);
//        ApiReturnVo res = sendExchangeRequest("/library/additionTestBatch", HttpMethod.POST, jsonParams, ApiReturnVo.class);
//        log.debug("{res}" + res);
//
//        if(!StringUtils.equals(res.getResultCode(), "100")) {
//            throw new Exception("Error : 유투바이오 추가검사 다건 생성/update 실패");
//        }
//    }
    
    /**
     * 예약가능 일자 조회
     * @param appointmentDatesPo
     * @return
     * @throws Exception
     */
    public List<String> getReserveDateList(U2bioReservAvailableDateRequest appointmentDatesPo) throws Exception {
        String jsonParams = objectMapper.writeValueAsString(appointmentDatesPo);
        String targetUrl = "/checkup/appointment/" + appointmentDatesPo.getStartDate() + "/" + appointmentDatesPo.getEndDate();
        U2bioReservAvailableDateResponse dateList = sendExchangeRequest(targetUrl, HttpMethod.POST, jsonParams, U2bioReservAvailableDateResponse.class);

        List<String> res = new ArrayList<>();
        if (dateList != null && !dateList.getAppointmentDatesWithTimes().isEmpty()) {
            res = dateList.getAppointmentDatesWithTimes().stream()
                    .filter(info -> "Y".equals(info.getEnableToAppoint()))
                    .flatMap(info -> {
                        boolean hasAM = info.getAppointmentTimes().stream()
                                .anyMatch(time -> time.getAppointmentTime().compareTo("12:00") < 0);
                        boolean hasPM = info.getAppointmentTimes().stream()
                                .anyMatch(time -> time.getAppointmentTime().compareTo("12:00") >= 0);

                        List<String> result = new ArrayList<>();
                        if (hasAM) {
                            result.add(info.getAppointmentDate().replace("-", "") + "AM");
                        }
                        if (hasPM) {
                            result.add(info.getAppointmentDate().replace("-", "") + "PM");
                        }
                        return result.stream();
                    })
                    .collect(Collectors.toList());
        }
        return res;
    }
    
    /**
     * 예약 가능 시간 조회 - 선택 일자, 오전/오후
     * @param appointmentDatesPo
     * @return
     * @throws Exception
     */
    public String getReserveAvailableTime(U2bioReservAvailableDateRequest appointmentDatesPo) throws Exception {
        String jsonParams = objectMapper.writeValueAsString(appointmentDatesPo);
        String targetUrl = "/checkup/appointment/" + appointmentDatesPo.getStartDate();
        U2bioReservAvailableTimeDto timeList = sendExchangeRequest(targetUrl, HttpMethod.POST, jsonParams, U2bioReservAvailableTimeDto.class);

        String res = null;
        if (timeList != null && !timeList.getAppointmentTimes().isEmpty()) {
            List<U2bioReservAvailableTimeDto.AppointmentTimes> appointmentTimes = timeList.getAppointmentTimes();
            String amPm = appointmentDatesPo.getAmPm();
            String earliestTime = "";
            for (U2bioReservAvailableTimeDto.AppointmentTimes time : appointmentTimes) {
                String[] parts = time.getAppointmentTime().split(":");
                int hour = Integer.parseInt(parts[0]);

                if (("AM".equals(amPm) && hour < 12) || ("PM".equals(amPm) && hour >= 12)) {
                    if (earliestTime.isEmpty() || time.getAppointmentTime().compareTo(earliestTime) < 0) {
                        earliestTime = time.getAppointmentTime();
                    }
                }
            }

            res = earliestTime.replace(":", "");
        }
        return res;
    }
    
    public String getPatientInfo(U2bioPatientDto u2bioPatientInfo) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("patientInfo", u2bioPatientInfo);
        String jsonParams = objectMapper.writeValueAsString(params);
        String targetUrl = "/users";
        U2bioPatientResponse res = sendExchangeRequest(targetUrl, HttpMethod.POST, jsonParams, U2bioPatientResponse.class);

        if(StringUtils.equals(res.getResultCode(), "100") && !StringUtils.isBlank(res.getResultMsg())) {
            return res.getResultMsg();
        } else {
            throw new Exception("Error : 유투바이오 차트 정보가져오기(update)");
        }
    }
    
    /**
     * 예약생성
     * @param reserveData
     * @return ReceiptId -> 예약번호
     * @throws Exception
     */
    public String setReservation(ReserveDataDto reserveData, HttpMethod httpMethod) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("reserveData", reserveData);
        String jsonParams = objectMapper.writeValueAsString(params);
        String targetUrl = "/checkup/patientNo/" + reserveData.getPatientInfo().getPatientNo() + "/appointment";

        U2bioPatientResponse res = sendExchangeRequest(targetUrl, httpMethod, jsonParams, U2bioPatientResponse.class);

        if(StringUtils.equals(res.getResultCode(), "100")) {
            return res.getResultMsg();
        } else {
            log.debug("Error : 예약 생성/변경 API" + res.getResultCode() + res.getResultMsg());
            throw new Exception(res.getResultCode());
        }
    }
    
    /**
     * 예약 취소 API
     * @param receiptId
     * @return
     * @throws Exception
     */
//    public String cancelReservation(String receiptId) throws Exception {
//        Map<String, Object> params = new HashMap<>();
//        params.put("receiptId", receiptId);
//        String jsonParams = objectMapper.writeValueAsString(params);
//        String targetUrl = "/checkup/appointment/" + receiptId + "/0";
//        ApiReturnVo res = sendExchangeRequest(targetUrl, HttpMethod.DELETE, jsonParams, ApiReturnVo.class);
//        if(StringUtils.equals(res.getResultCode(), "100")) {
//            return receiptId;
//        } else {
//            log.debug("Error : 예약 취소 API >>>> [" + res.getResultCode() + "] " + res.getResultMsg());
//            throw new Exception(res.getResultCode());
//        }
//    }

    /**
     * 예약 취소 가능 확인 API
     * @param receiptId
     * @return
     * @throws Exception
     */
//    public String cancelReservationCheck(String receiptId) throws Exception {
//        Map<String, Object> params = new HashMap<>();
//        params.put("receiptId", receiptId);
//        String jsonParams = objectMapper.writeValueAsString(params);
//        String targetUrl = "/checkup/appointment/cancelPossibility/" + receiptId;
//        ApiReturnVo res = sendExchangeRequest(targetUrl, HttpMethod.GET, jsonParams, ApiReturnVo.class);
//        if(StringUtils.equals(res.getResultCode(), "100")) {
//            return receiptId;
//        } else {
//            log.debug("Error : 예약 취소 API >>>> [" + res.getResultCode() + "] " + res.getResultMsg());
//            throw new Exception(res.getResultCode());
//        }
//    }

//    private HttpHeaders setResetHeaderInfo() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        String auth = commonExtInfo.getItgId() + ":" + commonExtInfo.getItgPwd();
//        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
//        String authHeader = "Basic " + new String(encodedAuth);
//
//        headers.set("Authorization", authHeader);
//        headers.set("ClientCode", "OMNI");
//
//        return headers;
//    }
//
//    private <T> T sendRequest(String url, HttpMethod method, String body, Class<T> responseType) throws Exception {
//        HttpHeaders headers = setResetHeaderInfo();
//        HttpEntity<String> entity = new HttpEntity<>(body, headers);
//        ResponseEntity<String> responseEntity;
//
//        log.debug("[유투바이오 API 전송]");
//        try {
//            log.debug("url : {}", commonExtInfo.getItgUrl() + url);
//            log.debug("id : {}", commonExtInfo.getItgId());
//            log.debug("body : {}", body);
//            responseEntity = restTemplate.exchange(commonExtInfo.getItgUrl() + url, method, entity, String.class);
//        } catch (HttpClientErrorException e) {
//            // 인증 관련 오류(401, 203 등)를 별도로 처리
//            HttpStatus statusCode = e.getStatusCode();
//            log.error("Error HttpClientErrorException : {}", e.getResponseBodyAsString());
//            if (statusCode == HttpStatus.UNAUTHORIZED || statusCode == HttpStatus.NON_AUTHORITATIVE_INFORMATION) {
//                throw new Exception(e.getResponseBodyAsString(), e);
//            }
//            throw new Exception(e.getResponseBodyAsString(), e);
//        } catch (Exception e) {
//            log.error("Error Exception : {}", e.getMessage());
//            throw new Exception(e.getMessage(), e);
//        }
//
//        log.debug("id : {}", responseEntity.getStatusCode());
//        log.debug("id : {}", responseEntity.getBody());
//
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            return objectMapper.readValue(responseEntity.getBody(), responseType);
//        } else {
//            throw new Exception("Error : " + responseEntity.getStatusCode());
//        }
//    }

    private <T> T sendExchangeRequest(String url, HttpMethod method, String body, Class<T> responseType) throws Exception {
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setUrl(commonExtInfo.getItgUrl() + url);
        exchangeRequest.setMethod(method.toString());
        exchangeRequest.setBody(body);
        Map<String, String> reqHeaders = new HashMap<>();
        reqHeaders.put("Content-Type", "application/json; charset=UTF-8"); // UTF-8 설정
        reqHeaders.put("Authorization", "Basic " + Base64.getEncoder().encodeToString(
                (commonExtInfo.getItgId() + ":" + commonExtInfo.getItgPwd()).getBytes(StandardCharsets.UTF_8)
        ));
        reqHeaders.put("ClientCode", "OMNI");

        ResponseEntity<String> responseEntity;
        try {
            exchangeRequest.setHeaders(reqHeaders);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

            HttpEntity<ExchangeRequest> entity = new HttpEntity<>(exchangeRequest, headers);

            System.out.printf("connectUrl : %s, connectPort : %s\n", connectUrl, connectPort);
            String targetUrl = connectUrl+":"+connectPort+"/exchange/send-request";
            responseEntity = restTemplate.exchange(targetUrl, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            // 인증 관련 오류(401, 203 등)를 별도로 처리
            HttpStatusCode statusCode = e.getStatusCode();
            log.error("Error HttpClientErrorException : {}", e.getResponseBodyAsString());
            if (statusCode == HttpStatus.UNAUTHORIZED || statusCode == HttpStatus.NON_AUTHORITATIVE_INFORMATION) {
                throw new Exception(e.getResponseBodyAsString(), e);
            }
            throw new Exception(e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Error Exception : {}", e.getMessage());
            throw new Exception(e.getMessage(), e);
        }

        log.debug("id : {}", responseEntity.getStatusCode());
        log.debug("id : {}", responseEntity.getBody());

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return objectMapper.readValue(responseEntity.getBody(), responseType);
        } else {
            throw new Exception("Error : " + responseEntity.getStatusCode());
        }
    }
	
}
