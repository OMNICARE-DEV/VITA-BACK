package com.hops.hops_new_api.common.model.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hops.hops_new_api.common.exception.HopsCode;
import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.model.HopsResponse;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

public class Connection {
    @SuppressWarnings("rawtypes")
    public <T, M> T request(M request, Class<T> res, String url, HttpMethod method) throws HopsException {

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        HttpEntity<M> req = new HttpEntity<>(request, header);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> response = null;

        try {
            response = restTemplate.exchange(url, method, req, res);
        } catch (HttpServerErrorException e) {
            try {
                HopsResponse result = new ObjectMapper().readValue(e.getResponseBodyAsString(), HopsResponse.class);
                throw new HopsException(result.getCode(), result.getMessage()); //연계 실패
            } catch (Exception e1) {
                throw new HopsException(HopsCode.PARSING_ERROR);
            }
        }

        return response.getBody();
    }


    public <T, M> ResponseEntity<T> exchange(M request, Class<T> res, String url, HttpMethod method) throws HopsException {

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        HttpEntity<M> req = new HttpEntity<>(request, header);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> response = null;

        try {
            response = restTemplate.exchange(url, method, req, res);
        } catch (HttpServerErrorException e) {
            try {
                HopsResponse result = new ObjectMapper().readValue(e.getResponseBodyAsString(), HopsResponse.class);
                throw new HopsException(result.getCode(), result.getMessage()); //연계 실패
            } catch (Exception e1) {
                throw new HopsException(HopsCode.PARSING_ERROR);
            }
        }

        return response;
    }
}
