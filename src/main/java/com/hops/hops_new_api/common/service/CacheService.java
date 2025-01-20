package com.hops.hops_new_api.common.service;

import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.model.Request.NiceCertificateAuthRequest;
import com.hops.hops_new_api.common.model.Request.NiceCertificationRequest;
import com.hops.hops_new_api.common.model.Request.RegKeyRequest;
import com.hops.hops_new_api.common.model.Response.NiceCertificateAuthResponse;
import com.hops.hops_new_api.common.model.Response.NiceCirtificationResponse;
import com.hops.hops_new_api.common.model.Response.RegKeyResponse;

public interface CacheService {
    // 캐시 데이터 저장
    public void saveCache(String key, Object value, long timeout);

    // 캐시 데이터 조회
    public Object getCache(String key);

    // 캐시 데이터 삭제
    public void deleteCache(String key);
}
