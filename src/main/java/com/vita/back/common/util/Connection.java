package com.vita.back.common.util;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vita.back.api.model.VitaResponse;
import com.vita.back.common.exception.VitaCode;
import com.vita.back.common.exception.VitaException;

@Component
public class Connection {
	
	@Value("${url.api}")
	private String API_URL;
	
    @SuppressWarnings("rawtypes")
    public <T, M> T request(M request, Class<T> res, String url, HttpMethod method) throws VitaException {

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        HttpEntity<M> req = new HttpEntity<>(request, header);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> response = null;

        try {
            response = restTemplate.exchange(url, method, req, res);
        } catch (HttpServerErrorException e) {
            try {
                VitaResponse result = new ObjectMapper().readValue(e.getResponseBodyAsString(), VitaResponse.class);
                throw new VitaException(result.getCode(), result.getMessage());
            } catch (Exception e1) {
                throw new VitaException(VitaCode.PARSING_ERROR);
            }
        }

        return response.getBody();
    }

    public <T, M> ResponseEntity<T> exchange(M request, Class<T> res, String url, HttpMethod method)
            throws VitaException {

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        HttpEntity<M> req = new HttpEntity<>(request, header);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> response = null;

        try {
            response = restTemplate.exchange(url, method, req, res);
        } catch (HttpServerErrorException e) {
            try {
                VitaResponse<?> result = new ObjectMapper().readValue(e.getResponseBodyAsString(), VitaResponse.class);
                throw new VitaException(result.getCode(), result.getMessage()); // 연계 실패
            } catch (Exception e1) {
                throw new VitaException(VitaCode.PARSING_ERROR);
            }
        }

        return response;
    }
}
