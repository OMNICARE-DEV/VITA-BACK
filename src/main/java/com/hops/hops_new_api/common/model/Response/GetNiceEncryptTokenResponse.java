package com.hops.hops_new_api.common.model.Response;

import com.hops.hops_new_api.config.CustomToStringStyle;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class GetNiceEncryptTokenResponse {
    private DataHeader dataHeader = new DataHeader();
    private DataBody dataBody = new DataBody();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
    }

    @Getter
    @Setter
    public static class DataHeader {
        private String GW_RSLT_CD = "";
        private String GW_RSLT_MSG = "";

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
        }
    }

    @Getter
    @Setter
    public static class DataBody {
        private String rsp_cd = "";
        private String res_msg = "";
        private String result_cd = "";
        private String site_code = "";
        private String token_version_id = "";
        private String token_val = "";
        private int period = 0;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, CustomToStringStyle.CUSTOM_STYLE);
        }
    }
}
