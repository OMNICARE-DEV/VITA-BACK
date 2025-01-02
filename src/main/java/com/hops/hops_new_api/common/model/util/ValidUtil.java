package com.hops.hops_new_api.common.model.util;

import com.hops.hops_new_api.common.exception.HopsCode;
import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.model.Constant.DataFormatEnum;
import org.apache.commons.lang3.StringUtils;

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
}
