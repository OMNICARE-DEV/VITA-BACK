package com.vita.back.api.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.vita.back.api.service.CacheService;

@Service
public class CacheServiceImpl implements CacheService {
    private final RedisTemplate<String, Object> redisTemplate;

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
