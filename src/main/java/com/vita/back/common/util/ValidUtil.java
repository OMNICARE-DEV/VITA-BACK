package com.vita.back.common.util;

import com.vita.back.api.model.Constant.DataFormatEnum;
import com.vita.back.common.exception.VitaCode;
import com.vita.back.common.exception.VitaException;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ValidUtil {
    public static void validNull(Object... datas) throws VitaException {
        for(Object data : datas) {
            if(data == null) {
                throw new VitaException(VitaCode.REQUIRED_VALUE_FAIL);
            }
            
            if(data instanceof String && StringUtils.isBlank((String)data)) {
                throw new VitaException(VitaCode.REQUIRED_VALUE_FAIL);
            }
            
            if (data instanceof List && ((List<?>) data).isEmpty()) {
                throw new VitaException(VitaCode.REQUIRED_VALUE_FAIL);
            }
            
            if (data.getClass().isArray() && ((Object[]) data).length == 0) {
                throw new VitaException(VitaCode.REQUIRED_VALUE_FAIL);
            }
        }
    }
    
    public static void validNull(String data, VitaCode code) throws VitaException {
        if(StringUtils.isBlank(data)) {
            throw new VitaException(code);
        }
    }
    
    public static void validFormat(String data, DataFormatEnum format) throws VitaException {
        if(!data.matches(format.getValidRegex())) {
            throw new VitaException(VitaCode.FORMAT_ERROR, "[" + data + "]" + format.getMessage());
        }
    }

    public static boolean isEmpty(String input) {
        return input == null || input.isEmpty() || input.trim().isEmpty();
    }

    public static boolean isEmpty(Object value) {
        return value == null;
    }

    public static boolean isEmpty(Object[] value) {
        return value == null || value.length == 0;
    }

    public static boolean isEmpty(List<?> value) {
        return value == null || value.isEmpty();
    }

    /** 만나이 계산기 */
    public static int getAmericanAge(String birthDate) {
        LocalDate now = LocalDate.now();
        LocalDate parsedBirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyyMMdd"));

        /* (1)2020-2007 = 13 */
        int americanAge = now.minusYears(parsedBirthDate.getYear()).getYear();

        /* 
         * (2)
         * 생일이 지났는지 여부를 판단하기 위해 (1)을 입력받은 생년월일의 연도에 더한다.
         * 연도가 같아짐으로 생년월일만 판단할 수 있다!
         */
        if (parsedBirthDate.plusYears(americanAge).isAfter(now)) {
            americanAge = americanAge -1;
        }

        return americanAge;
    }
}
