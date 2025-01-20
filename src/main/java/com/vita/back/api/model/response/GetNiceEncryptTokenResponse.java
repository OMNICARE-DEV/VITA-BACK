package com.vita.back.api.model.response;

import lombok.Data;

@Data
public class GetNiceEncryptTokenResponse {
    private DataHeader dataHeader = new DataHeader();
    private DataBody dataBody = new DataBody();

    @Data
    public static class DataHeader {
        private String GW_RSLT_CD;
        private String GW_RSLT_MSG;
    }

    @Data
    public static class DataBody {
        private String rsp_cd;
        private String res_msg;
        private String result_cd;
        private String site_code;
        private String token_version_id;
        private String token_val;
        private int period = 0;
    }
}
