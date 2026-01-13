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
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Profile("prod")
@Component
public class TelegramWebhookBotImpl extends TelegramWebhookBot implements BotSender {

    private static final Logger log = LoggerFactory.getLogger(TelegramWebhookBotImpl.class);

    private final CommandService commandService;
    private final UserService userService;
    private final TelegramBotProperties properties;

    public TelegramWebhookBotImpl(CommandService commandService,
                                  UserService userService,
                                  TelegramBotProperties properties) {
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
    public String getBotPath() {
        return properties.getWebhook().getPath();
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                return manageTextRequest(update);
            }
        } catch (Exception e) {
            Long chatId = extractChatId(update);
            log.error("Update handling error. chatId={}", chatId, e);

            if (chatId != null) {
                return SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("Произошла ошибка. Попробуйте позже.")
                        .build();
            }
        }
        return null;
    }

    private BotApiMethod<?> manageTextRequest(Update update) throws TelegramApiException {
        Message msg = update.getMessage();

        TelegramMessageDto dto = new TelegramMessageDto(
                msg.getChatId(),
                msg.getFrom().getId(),
                msg.getText(),
                msg.getFrom().getId(),
                msg.getFrom().getUserName(),
                msg.getFrom().getFirstName(),
                msg.getFrom().getLastName()
        );

        List<BotResponse> responses = commandService.handle(dto);
        return toSendMessage(responses);
    }

    private BotApiMethod<?> toSendMessage(List<BotResponse> responses) throws TelegramApiException {
        if (responses == null || responses.isEmpty()) {
            return null;
        }

        BotApiMethod<?> result = null;

        for (BotResponse response : responses) {
            BotApiMethod<?> method = toSendMessage(response);
            if (method != null) {
                if (result != null) {
                    execute(result);
                }
                result = method;
            }
        }
        return result;
    }

    private BotApiMethod<?> toSendMessage(BotResponse response) throws TelegramApiException {
        if (response == null) {
            return null;
        }

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

        // === EDIT EXISTING MESSAGE ===
        if (response.messageId() != null) {
            if (response.keyboard() != null) {
                userService.updateLastKeyboardMessageId(
                        response.chatId(),
                        response.messageId()
                );
            }

            return EditMessageText.builder()
                    .chatId(response.chatId().toString())
                    .messageId(response.messageId())
                    .text(response.text())
                    .replyMarkup(response.keyboard())
                    .build();
        }

        // === SEND NEW MESSAGE ===
        SendMessage send = SendMessage.builder()
                .chatId(response.chatId().toString())
                .text(response.text())
                .replyMarkup(response.keyboard())
                .build();

        Message sent = execute(send);

        if (response.keyboard() != null) {
            userService.updateLastKeyboardMessageId(
                    response.chatId(),
                    sent.getMessageId()
            );
        }

        return null;
    }

    private Long extractChatId(Update update) {
        try {
            if (update.hasMessage()) {
                return update.getMessage().getChatId();
            }
            if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null) {
                return update.getCallbackQuery().getMessage().getChatId();
            }
        } catch (Exception ignored) {
        }
        return null;
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public String botName() {
        return getBotUsername();
    }
}
