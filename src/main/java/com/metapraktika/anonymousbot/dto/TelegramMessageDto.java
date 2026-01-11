package com.metapraktika.anonymousbot.dto;

public class TelegramMessageDto {

    private final Long chatId;
    private final Long userId;
    private final String text;
    private final Long telegramUserId;
    private final String userName;
    private final String userFirstName;
    private final String userLastName;

    public TelegramMessageDto(Long chatId, Long userId, String text, Long telegramUserId, String userName, String userFirstName, String userLastName) {
        this.chatId = chatId;
        this.userId = userId;
        this.text = text;
        this.telegramUserId = telegramUserId;
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

    public Long getChatId() {
        return chatId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public Long getTelegramUserId() {

        return telegramUserId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }
}
