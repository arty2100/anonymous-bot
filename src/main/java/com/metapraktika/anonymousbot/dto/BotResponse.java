package com.metapraktika.anonymousbot.dto;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public record BotResponse(Long chatId, String text, Integer messageId, InlineKeyboardMarkup keyboard) {

    public BotResponse(Long chatId, String text) {
        this(chatId, text, null, null);
    }

    public BotResponse(
            Long chatId,
            String text,
            Integer messageId,
            InlineKeyboardMarkup keyboard
    ) {
        this.chatId = chatId;
        this.text = text;
        this.messageId = messageId;
        this.keyboard = keyboard;
    }
    public BotResponse(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        this(chatId, text, null, keyboard);
    }

    public BotResponse(Long chatId, String text, Integer messageId) {
        this(chatId, text, messageId, null);
    }

}