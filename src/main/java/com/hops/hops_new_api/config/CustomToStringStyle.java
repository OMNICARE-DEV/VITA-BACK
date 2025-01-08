package com.hops.hops_new_api.config;

import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class CustomToStringStyle extends ToStringStyle {
    public static final ToStringStyle CUSTOM_STYLE = new CustomToStringStyle();

    CustomToStringStyle() {
        super();
        this.setUseClassName(false);
        this.setUseIdentityHashCode(false);
        this.setContentStart("{" + System.lineSeparator() + "\t");
        this.setFieldNameValueSeparator(": ");
        this.setFieldSeparator("," + System.lineSeparator() + "\t");
        this.setContentEnd(System.lineSeparator() + "}");
    }

    @Override
    public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
        if (value == null) {
            appendFieldStart(buffer, fieldName);
            appendInternal(buffer, fieldName, "null", isFullDetail(fullDetail));
            appendFieldEnd(buffer, fieldName);
        } else if ("null".equals(value)) {
            appendFieldStart(buffer, fieldName);
            appendInternal(buffer, fieldName, "\"null\"", isFullDetail(fullDetail));
            appendFieldEnd(buffer, fieldName);
        } else if (value.getClass().isArray()) {
            Object[] array = (Object[]) value;
            if (array.length != 0) {
                appendFieldStart(buffer, fieldName);
                appendInternal(buffer, fieldName, value, isFullDetail(fullDetail));
                appendFieldEnd(buffer, fieldName);
            }
        } else if (value instanceof List<?>) {
            List<?> list = (List<?>) value;
            if (!list.isEmpty()) {
                appendFieldStart(buffer, fieldName);
                appendInternal(buffer, fieldName, value, isFullDetail(fullDetail));
                appendFieldEnd(buffer, fieldName);
            }
        } else {
            if (!value.equals("")) {
                appendFieldStart(buffer, fieldName);
                appendInternal(buffer, fieldName, value, isFullDetail(fullDetail));
                appendFieldEnd(buffer, fieldName);
            }
        }
    }
}
