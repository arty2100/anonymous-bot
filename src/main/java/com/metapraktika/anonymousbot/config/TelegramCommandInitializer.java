package com.metapraktika.anonymousbot.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class TelegramCommandInitializer {

    private final AbsSender bot;

    public TelegramCommandInitializer(AbsSender bot) {
        this.bot = bot;
    }

    @PostConstruct
    public void initCommands() {
        try {
            bot.execute(new SetMyCommands(
                    List.of(
                            new BotCommand("/help", "Помощь")
                    ),
                    new BotCommandScopeDefault(),
                    null
            ));
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Failed to init bot commands", e);
        }
    }

}
