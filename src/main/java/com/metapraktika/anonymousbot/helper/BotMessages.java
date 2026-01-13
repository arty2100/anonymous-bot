package com.metapraktika.anonymousbot.helper;

import com.metapraktika.anonymousbot.dto.BotResponse;
import org.springframework.stereotype.Component;

@Component
public class BotMessages {

    public BotResponse startMessage(Long chatId) {

        return new BotResponse(chatId, """
                ......задайте анонимный вопрос.....
                """);
    }

    public BotResponse forbiddenCommand(Long chatId) {
        return new BotResponse(chatId, "Команда недоступна, для просмотро доступных команд используйте  \n/help");
    }


    public BotResponse inviteUserAnswer(Long chatId, String link) {
        return new BotResponse(
                chatId,
                "Ссылка для приглашения пользователя:\n\n" + link
        );
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

    public BotResponse unknownCommand(Long chatId) {
        return new BotResponse(chatId, "Неизвестная команда, для просмотра доступных команд используйте команду \n/help");
    }

    public BotResponse userNotActive(Long chatId) {
        return new BotResponse(chatId, "Ваш аккаунт не активен. Ecли вы еще не зарегистрированы введите конманду /start либо обратитесь к администратору");
    }

    public BotResponse inviteSuperAdminAnswer(Long chatId, String link) {
        return new BotResponse(
                chatId,
                "Ссылка для приглашения супер администратора:\n\n" + link
        );
    }

    public BotResponse inviteAdminAnswer(Long chatId, String link) {
        return new BotResponse(
                chatId,
                "Ссылка для приглашения администратора:\n\n" + link
        );
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

    public BotResponse newMessageNotification(Long chatId, String text) {
        return new BotResponse(chatId, text);
    }
}
