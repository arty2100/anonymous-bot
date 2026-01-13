package com.metapraktika.anonymousbot.helper;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

public final class SecurityLogHelper {

    private static final Logger SECURITY_LOG =
            LoggerFactory.getLogger("SECURITY");

    private SecurityLogHelper() {
    }

    public static void logInvalidWebhook(
            HttpServletRequest request,
            Update update,
            String providedSecret
    ) {
        SECURITY_LOG.warn(
                "event=INVALID_WEBHOOK ip={} method={} path={} ua={} updateId={} chatId={} secretHash={}",
                getRemoteIp(request),
                request.getMethod(),
                request.getRequestURI(),
                request.getHeader("User-Agent"),
                update != null ? update.getUpdateId() : null,
                extractChatId(update),
                hashSecret(providedSecret)
        );
    }

    private static Long extractChatId(Update update) {
        if (update == null) {
            return null;
        }
        if (update.hasMessage() && update.getMessage().getChatId() != null) {
            return update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        return null;
    }

    private static String getRemoteIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static String hashSecret(String secret) {
        if (secret == null || secret.isBlank()) {
            return "null";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(secret.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash).substring(0, 12);
        } catch (Exception e) {
            return "hash_error";
        }
    }
}