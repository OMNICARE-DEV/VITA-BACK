package com.hops.hops_new_api.common.model.Request;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class GetNiceEncryptTokenRequest {
    private DataHeader dataHeader = new DataHeader();
    private DataBody dataBody = new DataBody();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }

    @Getter
    @Setter
    public static class DataHeader {
        private String CNTY_CD = "ko";

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
        }
    }

    @Getter
    @Setter
    public static class DataBody {
        private String req_dtim = "";
        private String req_no = "";
        private String enc_mode = "1";

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
        }
    }
}
