package com.metapraktika.anonymousbot.controller;

import com.metapraktika.anonymousbot.helper.SecurityLogHelper;
import com.metapraktika.anonymousbot.telegram.TelegramWebhookBotImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Profile("prod")
@RestController
@RequestMapping("${telegram.webhook.path}")
public class TelegramWebhookController {

    private static final Logger log = LoggerFactory.getLogger(TelegramWebhookController.class);
    private final TelegramWebhookBotImpl bot;
    private final String secret;

    public TelegramWebhookController(
            TelegramWebhookBotImpl bot,
            @Value("${telegram.webhook.secret}") String secret
    ) {
        log.info("REGISTERED");
        this.bot = bot;
        this.secret = secret;
    }

    @PostMapping
    public ResponseEntity<?> onUpdate(
            @RequestBody Update update,
            @RequestHeader(value = "X-Telegram-Bot-Api-Secret-Token", required = false) String headerSecret,
            HttpServletRequest request
    ) {
        log.info("ON UPDATE ARRIVED");
        if (!secret.equals(headerSecret)) {
            SecurityLogHelper.logInvalidWebhook(request, update, headerSecret);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        BotApiMethod<?> response = bot.onWebhookUpdateReceived(update);

        if (response == null) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok(response);
    }
}
