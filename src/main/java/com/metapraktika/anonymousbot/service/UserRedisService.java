package com.metapraktika.anonymousbot.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRedisService {

    private final StringRedisTemplate redis;

    public UserRedisService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    private String kbKey(long telegramUserId) {
        return "user:kb:" + telegramUserId;
    }

    public void saveLastKeyboardMessageId(long telegramUserId, int messageId) {
        redis.opsForValue().set(kbKey(telegramUserId), String.valueOf(messageId));
    }

    public Optional<Integer> getLastKeyboardMessageId(long telegramUserId) {
        String value = redis.opsForValue().get(kbKey(telegramUserId));
        if (value == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            redis.delete(kbKey(telegramUserId));
            return Optional.empty();
        }
    }

    public void deleteLastKeyboardMessageId(long telegramUserId) {
        redis.delete(kbKey(telegramUserId));
    }
}
