package com.metapraktika.anonymousbot.service;

import com.metapraktika.anonymousbot.dto.BotResponse;
import org.springframework.stereotype.Component;

@Component
public class BotMessages {

    public BotResponse startMessage(Long chatId) {

        return new BotResponse(chatId, """
                ......задайте анонимный вопрос.....
                """);
    }


    public BotResponse messageRegistered(Long chatId) {
        return new BotResponse(chatId, "Ваше сообщение принято! ");
    }

    public BotResponse rateLimitExceeded(Long chatId) {
        return new BotResponse(chatId, "Вы превысили лимит команд. Пожалуйста, подождите минуту.");
    }

    public BotResponse emptyCommand(Long chatId) {
        return new BotResponse(chatId, "Введите сообщение");
    }

    public BotResponse userBlocked(Long chatId) {
        return new BotResponse(chatId, "Ваш аккаунт не заблокирован. Обратитесь к администратору");
    }

    public BotResponse inviteRequired(Long chatId) {
        return new BotResponse(chatId, "Доступ только по приглашению.");
    }

    public BotResponse inviteNotFound(Long chatId) {
        return new BotResponse(chatId, "Приглашение не существует");
    }

    public BotResponse inviteAlreadyUsed(Long chatId) {
        return new BotResponse(chatId, "Приглашение уже использовано");
    }

    public BotResponse roleUpdatedAdmin(Long chatId) {
        return new BotResponse(
                chatId,
                "Поздравляем, вы стали админом, для списка доступных команд нажмите \n/help"
        );
    }

    public BotResponse congratsSuperAdmin(Long chatId) {
        return new BotResponse(
                chatId,
                "Поздравляем, вы приняты в группу в роли супер админа, для списка доступных команд нажмите \n/help"
        );
    }

    public BotResponse congratsAdmin(Long chatId) {
        return new BotResponse(
                chatId,
                "Поздравляем, вы приняты в группу в роли админа, для списка доступных команд нажмите \n/help"
        );
    }


    public BotResponse inviteExpired(Long chatId) {
        return new BotResponse(chatId, "Приглашение уже просрочено");
    }

    public BotResponse inviteAccepted(Long chatId) {
        return new BotResponse(chatId, "");
    }

    public BotResponse roleUpdatedSuperAdmin(Long chatId) {
        return new BotResponse(
                chatId,
                "Поздравляем, вы стали супер админом, для списка доступных команд нажмите \n/help"
        );
    }

    public BotResponse alreadyAdmin(Long chatId) {
        return new BotResponse(
                chatId,
                "Вы уже являетесь админом"
        );
    }

    public BotResponse errorOccurred(Long chatId) {
        return new BotResponse(
                chatId,
                "Произошла ошибка. Попробуйте позже."
        );

    }
}
