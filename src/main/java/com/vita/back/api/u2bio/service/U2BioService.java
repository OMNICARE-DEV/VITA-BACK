package com.vita.back.api.u2bio.service;

import java.util.List;

import org.springframework.http.HttpMethod;

import com.vita.back.api.model.data.ExtIntegrationDto;
import com.vita.back.api.reserv.model.data.ReserveDataDto;
import com.vita.back.api.u2bio.model.data.U2bioPatientDto;
import com.vita.back.api.u2bio.model.request.U2bioReservAvailableDateRequest;

public interface U2BioService {

    /**
     * 공통 연동 정보 설정
     * @param commonExtInfo CommonExtInfo 객체
     */
    void setCommonExtInfo(ExtIntegrationDto commonExtInfo);

    /**
     * 예약 가능 일자 조회
     * @param appointmentDatesPo 예약 일자 요청 객체
     * @return 예약 가능한 날짜 리스트
     * @throws Exception 예외
     */
    List<String> getReserveDateList(U2bioReservAvailableDateRequest appointmentDatesPo) throws Exception;

    /**
     * 예약 가능 시간 조회
     * @param appointmentDatesPo 예약 시간 요청 객체
     * @return 예약 가능한 시간
     * @throws Exception 예외
     */
    String getReserveAvailableTime(U2bioReservAvailableDateRequest appointmentDatesPo) throws Exception;

	String getPatientInfo(U2bioPatientDto patientInfo) throws Exception;

	String setReservation(ReserveDataDto reserveData, HttpMethod post) throws Exception;

    // LibCheckupTestRo searchCheckupTest(LibCheckupTestPo params) throws Exception;
    // String setCheckupItemCode(LibProfile libProfile) throws Exception;
    // String setAdditionItemCode(LibProfile libProfile) throws Exception;
    // void setAdditionItemCodeList(LibAdditionTestPo libAdditionTestPo) throws Exception;
    // String getPatientInfo(U2bioPatientInfo u2bioPatientInfo) throws Exception;
    // String setReservation(ReserveData reserveData, HttpMethod httpMethod) throws Exception;
    // String cancelReservation(String receiptId) throws Exception;
    // String cancelReservationCheck(String receiptId) throws Exception;
}
