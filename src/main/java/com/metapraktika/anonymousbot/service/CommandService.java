package com.metapraktika.anonymousbot.service;

import com.metapraktika.anonymousbot.dto.BotResponse;
import com.metapraktika.anonymousbot.dto.TelegramMessageDto;
import com.metapraktika.anonymousbot.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class CommandService {

    private static final Logger log =
            LoggerFactory.getLogger(CommandService.class);

    private final RateLimiterService rateLimiterService;
    private final UserService userService;
    private final BotMessages botMessages;

    public CommandService(RateLimiterService rateLimiterService, UserService userService, BotMessages botMessages) {
        this.rateLimiterService = rateLimiterService;
        this.userService = userService;
        this.botMessages = botMessages;
    }

    @Transactional
    public List<BotResponse> handle(TelegramMessageDto message) throws TelegramApiException {

        String text = message.getText();
        Long chatId = message.getChatId();
        User user = getOrCreteUser(message);

        if (!rateLimiterService.isAllowed(user.getTelegramId(), user.getRole().getName())) {
            //  return List.of(botMessages.rateLimitExceeded(chatId));
        }

        return List.of(botMessages.messageRegistered(chatId));
    }

    private User getOrCreteUser(TelegramMessageDto message) {
        return userService.getOrCreate(
                message.getTelegramUserId(),
                message.getUserName(),
                message.getUserFirstName(),
                message.getUserLastName()
        );
    }


}
