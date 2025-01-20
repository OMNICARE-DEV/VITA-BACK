package com.vita.back.api.service;

public interface CacheService {
    /** 캐시 데이터 저장 */
    public void saveCache(String key, Object value, long timeout);
    /** 캐시 데이터 조회 */
    public Object getCache(String key);
    /** 캐시 데이터 삭제 */
    public void deleteCache(String key);
}
