package org.aibles.auth.utils.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
@Slf4j
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void save(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Optional<Object> get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }



    @Override
    public void save(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void delete(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    @Override
    public Optional<Object> get(String key, String hashKey) {
        return Optional.ofNullable(redisTemplate.opsForHash().get(key, hashKey));
    }

    @Override
    public <T> T getOrDefault(String key, String hashKey, T defaultValue) {
        HashOperations<String, Object, Object> hashOps = stringRedisTemplate.opsForHash();
        Object result = hashOps.get(key, hashKey);
        log.info("Retrieved OTP from Redis - key: {}, hashKey: {}, value: {}", key, hashKey, result);
        return (result != null) ? (T) result : defaultValue;
    }


    @Override
    public void saveWithExpire(String key, String hashKey, String value, Long timeout) {
        HashOperations<String, Object, Object> hashOps = stringRedisTemplate.opsForHash();
        hashOps.put(key, hashKey, value);
        stringRedisTemplate.expire(key, timeout, TimeUnit.MINUTES);
        log.info("Saved OTP to Redis - key: {}, hashKey: {}, value: {}, timeout: {}", key, hashKey, value, timeout);
    }
    @Override
    public String findOtp(String username, String hashKey) {
        return getValue(username, hashKey);
    }
    @Override
    public void clearActiveOtp(String username) {
        stringRedisTemplate.delete(username);
    }
    public String getValue(String key, String hashKey) {
        HashOperations<String, Object, Object> hashOps = stringRedisTemplate.opsForHash();
        return (String) hashOps.get(key, hashKey);
    }

    @Override
    public String findToken(String username, String hashKey) {
        return getValue(username, hashKey);
    }

}