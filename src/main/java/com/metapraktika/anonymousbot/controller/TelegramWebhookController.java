package com.metapraktika.anonymousbot.controller;


import com.metapraktika.anonymousbot.helper.SecurityLogHelper;
import com.metapraktika.anonymousbot.telegram.TelegramWebhookBotImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Profile("prod")
@RestController
@RequestMapping("${telegram.webhook.path}")
public class TelegramWebhookController {

    private final TelegramWebhookBotImpl bot;
    private final String secret;

    public TelegramWebhookController(
            TelegramWebhookBotImpl bot,
            @Value("${telegram.webhook.secret}") String secret
    ) {
        this.bot = bot;
        this.secret = secret;
    }

    @PostMapping
    public BotApiMethod<?> onUpdate(
            @RequestBody Update update,
            @RequestHeader(value = "X-Telegram-Bot-Api-Secret-Token", required = false) String headerSecret,
            HttpServletRequest request
    ) {
        if (!secret.equals(headerSecret)) {
            SecurityLogHelper.logInvalidWebhook(request, update, headerSecret);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return bot.onWebhookUpdateReceived(update);
    }
}
