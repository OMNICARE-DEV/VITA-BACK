package com.hops.hops_new_api.common.service.impl;

import com.hops.hops_new_api.common.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {
    private static final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public CacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveCache(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
    }

    @Override
    public Object getCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }
}
