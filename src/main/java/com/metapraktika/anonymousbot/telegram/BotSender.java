package com.metapraktika.anonymousbot.telegram;

public interface BotSender {
    void sendToChat(Long chatId, String text);
    String botName();

}
