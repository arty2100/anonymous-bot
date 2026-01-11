package com.metapraktika.anonymousbot.dto;

public class TelegramCallbackDto {
    private final Long chatId;
    private final Long telegramUserId;
    private final String data;
    private final String userName;
    private final String userFirstName;
    private final String userLastName;
    private final Integer messageId;

    public TelegramCallbackDto(Long chatId, Long telegramUserId, String data, String userName, String userFirstName, String userLastName, Integer messageId) {
        this.chatId = chatId;
        this.telegramUserId = telegramUserId;
        this.data = data;
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.messageId = messageId;
    }

    public Long getChatId() {
        return chatId;
    }

    public Long getTelegramUserId() {
        return telegramUserId;
    }

    public String getData() {
        return data;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public Integer getMessageId() {return messageId;}
}
