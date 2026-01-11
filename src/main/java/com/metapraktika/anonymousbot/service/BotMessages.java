package com.metapraktika.anonymousbot.service;

import com.metapraktika.anonymousbot.dto.BotResponse;
import org.springframework.stereotype.Component;

@Component
public class BotMessages {

    public BotResponse messageRegistered(Long chatId) {
        return new BotResponse(chatId, "Ваше сообщение принято! ");
    }

}
