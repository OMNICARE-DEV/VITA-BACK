package com.hops.hops_new_api.common.model.util;

import com.hops.hops_new_api.common.exception.HopsCode;
import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.model.Constant.DataFormatEnum;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ValidUtil {
    public static void validNull(Object... datas) throws HopsException {
        for(Object data : datas) {
            if(data == null) {
                throw new HopsException(HopsCode.REQUIRED_VALUE_FAIL);
            }
            
            if(data instanceof String && StringUtils.isBlank((String)data)) {
                throw new HopsException(HopsCode.REQUIRED_VALUE_FAIL);
            }
            
            if (data instanceof List && ((List<?>) data).isEmpty()) {
                throw new HopsException(HopsCode.REQUIRED_VALUE_FAIL);
            }
            
            if (data.getClass().isArray() && ((Object[]) data).length == 0) {
                throw new HopsException(HopsCode.REQUIRED_VALUE_FAIL);
            }
        }
    }
    
    public static void validNull(String data, HopsCode code) throws HopsException {
        if(StringUtils.isBlank(data)) {
            throw new HopsException(code);
        }
    }
    
    public static void validFormat(String data, DataFormatEnum format) throws HopsException {
        if(!data.matches(format.getValidRegex())) {
            throw new HopsException(HopsCode.FORMAT_ERROR, "[" + data + "]" + format.getMessage());
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

    // 만나이 계산기
    public static int getAmericanAge(String birthDate) {
        LocalDate now = LocalDate.now();
        LocalDate parsedBirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyyMMdd"));

        int americanAge = now.minusYears(parsedBirthDate.getYear()).getYear(); // (1)2020-2007 = 13

        // (2)
        // 생일이 지났는지 여부를 판단하기 위해 (1)을 입력받은 생년월일의 연도에 더한다.
        // 연도가 같아짐으로 생년월일만 판단할 수 있다!
        if (parsedBirthDate.plusYears(americanAge).isAfter(now)) {
            americanAge = americanAge -1;
        }

        return americanAge;
    }
}
