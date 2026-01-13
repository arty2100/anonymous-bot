package com.metapraktika.anonymousbot.helper;

import com.metapraktika.anonymousbot.dto.BotResponse;
import org.springframework.stereotype.Component;

@Component
public class BotMessages {

    public BotResponse startMessage(Long chatId) {

        return new BotResponse(chatId,
                "Небольшие условности:\n\n" +
                        "<b>• Я читаю все сообщения</b>, но могу отвечать не сразу.\n\n" +

                        "<b>• Никаких диагнозов</b> и вмешательств в вашу психику.\n\n" +

                        "<b>• Я отвечаю</b>, репостя сообщения в группу " +
                        "<a href=\"https://t.me/rankazazhivi_chat\">@rankazazhivi_chat</a>,\n" +
                        "только в пределах своей компетенции и в поддерживающем формате.\n\n" +

                        "<b>• Всё, что вы напишете здесь</b>, остаётся <b>гарантированно анонимным</b>\n" +
                        "и в рамках этики (к которой я отношусь строго),\n" +
                        "а также технически - потому что бот только мой.\n\n" +

                        "<b>• Вы можете получить поддержку</b> от участников комьюнити (подписчиков).\n" +
                        "Я бережно слежу за тем, чтобы это было экологично,\n" +
                        "ведь ваша психика - самое важное и хрупкое,\n" +
                        "а вы - главная ценность сообщества и моя.\n\n" +

                        "<b>• На некоторые сообщения</b> я не смогу ответить по этическим причинам\n" +
                        "или из-за выхода за пределы моей компетенции.\n" +
                        "Позже у бота появятся расширяющие функции,\n" +
                        "и я смогу отвечать вам лично, не зная, кому именно отвечаю.\n\n" +

                        "<b>• В основном здесь тема психологии</b>\n" +
                        "<i>(клинической, кризисной, КПТ)</i>,\n" +
                        "но если хочется выругаться на погоду, на стоимость продуктов,\n" +
                        "похвалить кого-то из подписчиков или незнакомца,\n" +
                        "разместить свои чувства или просто написать что-то анонимно -\n" +
                        "<b>пожалуйста 🙂</b>\n\n" +

                        "<i>Спасибо за внимание к себе и к этому пространству</i> ❤️‍🔥"
        );

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
