package com.hops.hops_new_api.common.model.Request;

import lombok.Data;

@Data
public class GetNiceEncryptTokenRequest {
    private DataHeader dataHeader = new DataHeader();
    private DataBody dataBody = new DataBody();

    @Data
    public static class DataHeader {
        private String CNTY_CD = "ko";
    }

    @Data
    public static class DataBody {
        private String req_dtim = "";
        private String req_no = "";
        private String enc_mode = "1";
    }
}
