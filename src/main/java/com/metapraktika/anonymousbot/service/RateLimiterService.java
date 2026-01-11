package com.metapraktika.anonymousbot.service;

import com.metapraktika.anonymousbot.enums.RoleType;
import com.metapraktika.anonymousbot.properties.RateLimitProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;
    private final RateLimitProperties rateLimitProperties;

    public RateLimiterService(StringRedisTemplate redisTemplate, RateLimitProperties rateLimitProperties) {
        this.redisTemplate = redisTemplate;
        this.rateLimitProperties = rateLimitProperties;
    }

    public boolean isAllowed(Long telegramId, RoleType role) {
        String key = "rate_limit:" + telegramId;
        Integer limit = rateLimitProperties.getLimits().getOrDefault(role, 10);

        Long count = redisTemplate.opsForValue().increment(key);
        if (count == null) {
            return true;
        }

        if (count == 1) {
            redisTemplate.expire(key, Duration.ofMinutes(1));
        }

        return count <= limit;
    }
}
