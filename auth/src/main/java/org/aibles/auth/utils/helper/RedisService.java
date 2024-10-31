package org.aibles.auth.utils.helper;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


public interface RedisService {
    void save(String key, Object value, long timeout, TimeUnit unit);
    void delete(String key);
    Optional<Object> get(String key);

    void save(String key, String hashKey, Object value);
    void delete(String key, String hashKey);
    Optional<Object> get(String key, String hashKey);
    <T> T getOrDefault(String key, String hashKey, T defaultValue);
    void saveWithExpire(String key, String hashKey, String value, Long timeout);

    String findOtp(String username, String hashKey);
    String findToken(String username, String hashKey);

    void clearActiveOtp(String username);
}

