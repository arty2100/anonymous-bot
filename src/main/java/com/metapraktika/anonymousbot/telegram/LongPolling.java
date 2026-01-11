package com.metapraktika.anonymousbot.telegram;

import com.metapraktika.anonymousbot.config.TelegramBotProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Profile("dev")
@Component
public class LongPolling extends TelegramLongPollingBot {

    private final Logger log = LoggerFactory.getLogger(LongPolling.class);

    private final TelegramBotProperties telegramBotProperties;

    public LongPolling(TelegramBotProperties telegramBotProperties) {
        this.telegramBotProperties = telegramBotProperties;
    }

    @Override
    public void onUpdateReceived(Update update) {

        System.out.println("onUpdateReceived");
    }

    public String getBotToken() {
        return telegramBotProperties.getToken();
    }

    @Override
    public String getBotUsername() {

        return telegramBotProperties.getUsername();
    }
}
