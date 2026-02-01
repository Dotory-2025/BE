package com.dotoryteam.dotory.global.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SecurityRedisService {
    private final RedisTemplate<String, String> redisTemplate;

    //redis 에 refresh token 저장
    public void setValue(String key, String token , long duration) {
        ValueOperations<String , String> ops = redisTemplate.opsForValue();
        ops.set(key, token , duration , TimeUnit.MILLISECONDS);
    }

    //refreshToken 가져오는 로직
    public String getValue(String key) {
        ValueOperations<String , String> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    //refreshToken 삭제
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    //블랙리스트 등록
    public void setBlackList(String key , String token , long duration) {
        ValueOperations<String , String> ops = redisTemplate.opsForValue();
        ops.set(key , token , duration , TimeUnit.MILLISECONDS);
    }

    //블랙리스트 여부
    public boolean isBlackList(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
