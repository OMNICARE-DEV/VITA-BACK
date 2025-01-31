package com.vita.back.api.u2bio.model.request;

import java.util.Map;

import lombok.Data;

@Data
public class ExchangeRequest {
	/** 전송 대상 url */
    private String url;
    /** curl method */
    private String method;
    /** 전송 body */
    private String body;
    /** 전송 header */
    private Map<String, String> headers;
}
