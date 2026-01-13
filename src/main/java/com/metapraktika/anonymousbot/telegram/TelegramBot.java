package com.metapraktika.anonymousbot.telegram;


import com.metapraktika.anonymousbot.dto.BotResponse;
import com.metapraktika.anonymousbot.dto.TelegramMessageDto;
import com.metapraktika.anonymousbot.properties.TelegramBotProperties;
import com.metapraktika.anonymousbot.service.CommandService;
import com.metapraktika.anonymousbot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Profile("dev")
@Component
public class TelegramBot extends TelegramLongPollingBot implements BotSender {
    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);
    private final CommandService commandService;
    private final UserService userService;

    private final TelegramBotProperties properties;


    public TelegramBot(CommandService commandService,  UserService userService, TelegramBotProperties properties) {
        this.commandService = commandService;
        this.userService = userService;
        this.properties = properties;
    }

    @Override
    public String getBotUsername() {
        return properties.getUsername();
    }

    @Override
    public String getBotToken() {
        return properties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        try {
            if (update.hasMessage() && update.getMessage().hasText()) {

                manageTextRequest(update);
                return;
            }

        } catch (Exception e) {

            Long chatId = null;
            try {
                if (update != null) {
                    if (update.hasMessage() && update.getMessage() != null) {
                        chatId = update.getMessage().getChatId();
                    } else if (update.hasCallbackQuery() && update.getCallbackQuery() != null && update.getCallbackQuery().getMessage() != null) {
                        chatId = update.getCallbackQuery().getMessage().getChatId();
                    }
                }
            } catch (Exception ex) {
                log.error("Failed to send error message", ex);
            }

            if (chatId != null) {
                log.error("Update handling error. chatId={}", chatId, e);

                SendMessage errorMessage = SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("Произошла ошибка. Попробуйте позже.")
                        .parseMode("HTML")
                        .build();

                try {
                    execute(errorMessage);
                } catch (TelegramApiException ex) {
                    log.error("Failed to send error message", ex);
                }
            } else {
                log.error("Update handling error. chatId is unknown", e);
            }
        }
    }

    private void manageTextRequest(Update update) throws TelegramApiException {
        Message msg = update.getMessage();

        TelegramMessageDto messageDto = new TelegramMessageDto(msg.getChatId(), msg.getFrom().getId(), msg.getText(), msg.getFrom().getId(), msg.getFrom().getUserName(), msg.getFrom().getFirstName(), msg.getFrom().getLastName());

        List<BotResponse> responses = commandService.handle(messageDto);
        if (responses != null) {
            for (BotResponse response : responses) {
                send(response);
            }
        }
    }

    private void send(BotResponse response) throws TelegramApiException {
        if (response == null) return;

        Integer lastKbMessageId = userService.getLastKeyboardMessageId(response.chatId());

        if (lastKbMessageId != null) {
            try {
                EditMessageReplyMarkup clear = new EditMessageReplyMarkup();
                clear.setChatId(response.chatId().toString());
                clear.setMessageId(lastKbMessageId);
                clear.setReplyMarkup(null);
                execute(clear);
            } catch (TelegramApiException e) {
                log.warn("Could not clear previous keyboard for messageId={}", lastKbMessageId);
            }
            userService.clearLastKeyboardMessageId(response.chatId());
        }

        if (response.messageId() != null) {
            EditMessageText edit = new EditMessageText();
            edit.setChatId(response.chatId().toString());
            edit.setMessageId(response.messageId());
            edit.setText(response.text());
            edit.setParseMode("HTML");
            edit.setReplyMarkup(response.keyboard());
            Message executed = (Message) execute(edit);
            if (response.keyboard() != null) {
                userService.updateLastKeyboardMessageId(response.chatId(), response.messageId());
            }
            return;
        }

        SendMessage send = new SendMessage();
        send.setChatId(response.chatId().toString());
        send.setText(response.text());
        send.setParseMode("HTML");
        send.setReplyMarkup(response.keyboard());
        Message sent = execute(send);
        if (response.keyboard() != null) {
            userService.updateLastKeyboardMessageId(response.chatId(), sent.getMessageId());
        }
    }

    private void modifyPreviousMessage(BotResponse response) throws TelegramApiException {
        if (response.messageId() != null) {
            EditMessageReplyMarkup editMessage = new EditMessageReplyMarkup();
            editMessage.setChatId(response.chatId());
            editMessage.setMessageId(response.messageId());
            editMessage.setReplyMarkup(null);
            execute(editMessage);
        }
    }

    @Override
    public void sendToChat(Long chatId, String text) {
        SendMessage msg = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .parseMode("HTML")
                .build();

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            log.error("Failed to send scheduled message. chatId={}", chatId, e);
            throw new RuntimeException("Telegram API error", e);
        }
    }

    @Override
    public String botName() {
        return getBotUsername();
    }
}