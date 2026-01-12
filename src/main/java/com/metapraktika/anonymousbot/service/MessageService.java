package com.metapraktika.anonymousbot.service;

import com.metapraktika.anonymousbot.dto.BotResponse;
import com.metapraktika.anonymousbot.entity.Message;
import com.metapraktika.anonymousbot.entity.MessageToAdmin;
import com.metapraktika.anonymousbot.entity.User;
import com.metapraktika.anonymousbot.enums.RoleType;
import com.metapraktika.anonymousbot.helper.BotMessages;
import com.metapraktika.anonymousbot.repository.MessageRepository;
import com.metapraktika.anonymousbot.repository.MessageToAdminRepository;
import com.metapraktika.anonymousbot.repository.UserRepository;
import com.metapraktika.anonymousbot.telegram.BotSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;
    private final MessageToAdminRepository messageToAdminRepository;
    private final UserRepository userRepository;
    private final BotMessages botMessages;
    private final BotSender botSender;

    public MessageService(MessageRepository messageRepository,
                          MessageToAdminRepository messageToAdminRepository,
                          UserRepository userRepository,
                          BotMessages botMessages,
                          @Lazy BotSender botSender) {
        this.messageRepository = messageRepository;
        this.messageToAdminRepository = messageToAdminRepository;
        this.userRepository = userRepository;
        this.botMessages = botMessages;
        this.botSender = botSender;
    }

    @Transactional
    public List<BotResponse> saveAndNotifyAdmins(User sender, String text) {
        Message message = new Message(sender, text);
        messageRepository.save(message);

        List<User> admins = userRepository.findAllByRoleNameIn(Set.of(RoleType.ADMIN, RoleType.SUPER_ADMIN));
        String notificationText = botMessages.newMessageNotification(0L, text).text();

        for (User admin : admins) {
            MessageToAdmin messageToAdmin = new MessageToAdmin(message, admin);
            messageToAdminRepository.save(messageToAdmin);

            try {
                botSender.sendToChat(admin.getTelegramId(), notificationText);
                messageToAdmin.markSent();
            } catch (Exception e) {
                log.error("Failed to send message to admin: {}", admin.getTelegramId(), e);
                messageToAdmin.markFailed();
            }
            messageToAdminRepository.save(messageToAdmin);
        }

        return List.of(botMessages.messageRegistered(sender.getTelegramId()));
    }
}
