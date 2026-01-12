package com.metapraktika.anonymousbot.service;

import com.metapraktika.anonymousbot.dto.BotResponse;
import com.metapraktika.anonymousbot.dto.TelegramMessageDto;
import com.metapraktika.anonymousbot.entity.Invite;
import com.metapraktika.anonymousbot.entity.Role;
import com.metapraktika.anonymousbot.entity.User;
import com.metapraktika.anonymousbot.enums.RoleType;
import com.metapraktika.anonymousbot.enums.UserStatus;
import com.metapraktika.anonymousbot.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CommandService {

    private static final Logger log =
            LoggerFactory.getLogger(CommandService.class);

    private final RateLimiterService rateLimiterService;
    private final UserService userService;
    private final BotMessages botMessages;
    private final InviteService inviteService;
    private final RoleRepository roleRepository;

    public CommandService(RateLimiterService rateLimiterService, UserService userService, BotMessages botMessages, InviteService inviteService, RoleRepository roleRepository) {
        this.rateLimiterService = rateLimiterService;
        this.userService = userService;
        this.botMessages = botMessages;
        this.inviteService = inviteService;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public List<BotResponse> handle(TelegramMessageDto message) throws TelegramApiException {

        String text = message.getText();
        Long chatId = message.getChatId();
        User user = getOrCreteUser(message);

        if (!rateLimiterService.isAllowed(user.getTelegramId(), user.getRole().getName())) {
            return List.of(botMessages.rateLimitExceeded(chatId));
        }
        if (text == null || text.isBlank()) {
            return List.of(botMessages.emptyCommand(chatId));
        }

        if (text.startsWith("/start")) {

            return handleStartCommand(message, user, chatId);
        }

        return List.of(botMessages.messageRegistered(chatId));
    }

    private List<BotResponse> handleStartCommand(
            TelegramMessageDto message,
            User user,
            Long chatId
    ) {
        if (user.getStatus() == UserStatus.BLOCKED) {
            return List.of(botMessages.userBlocked(chatId));
        }

        HashMap<Boolean, List<BotResponse>> invitationResponse =
                getInvitationResponse(user, message);

        if (invitationResponse.containsKey(true)) {
            return invitationResponse.get(true);
        }

        if (invitationResponse.containsKey(false)) {
            return invitationResponse.get(false);
        }

        user.setStatus(UserStatus.ACTIVE);
        return List.of(botMessages.messageRegistered(chatId));
    }

    public HashMap<Boolean, List<BotResponse>> getInvitationResponse(
            User user,
            TelegramMessageDto message
    ) {

        HashMap<Boolean, List<BotResponse>> responseMap = new HashMap<>();

        Long chatId = message.getChatId();
        String[] parts = message.getText().split(" ");

        if (parts.length < 2) {
            return responseMap;
        }

        String inviteCode = parts[1];
        if (inviteCode.startsWith("invite_")) {
            inviteCode = inviteCode.substring("invite_".length());
        }
        Invite invitation = inviteService.getInvitation(inviteCode);

        if (invitation == null) {
            responseMap.put(false, List.of(botMessages.inviteNotFound(chatId)));
            return responseMap;
        }

        if (invitation.getIsUsed()) {
            responseMap.put(false, List.of(botMessages.inviteAlreadyUsed(chatId)));
            return responseMap;
        }

        if (invitation.isExpired()) {
            responseMap.put(false, List.of(botMessages.inviteExpired(chatId)));
            return responseMap;
        }

        RoleType targetRole = invitation.getTargetRole();
        RoleType currentRole = user.getRole().getName();

        if (currentRole == targetRole) {
            inviteService.setUsed(user, invitation);
            responseMap.put(false, List.of(botMessages.alreadyAdmin(chatId)));
            return responseMap;
        }

        if (currentRole == RoleType.SUPER_ADMIN && targetRole == RoleType.ADMIN) {
            inviteService.setUsed(user, invitation);
            responseMap.put(false, List.of(botMessages.alreadyAdmin(chatId)));
            return responseMap;
        }

        if (targetRole == RoleType.USER) {
            user.setStatus(UserStatus.ACTIVE);
            inviteService.setUsed(user, invitation);
            responseMap.put(true, List.of(botMessages.startMessage(chatId)));
            return responseMap;
        }

        if (targetRole == RoleType.ADMIN || targetRole == RoleType.SUPER_ADMIN) {

            Optional<Role> roleOpt = roleRepository.findByName(targetRole);
            if (roleOpt.isEmpty()) {
                log.error("Role {} not exists", targetRole.name());
                responseMap.put(false, List.of(botMessages.errorOccurred(chatId)));
                return responseMap;
            }

            boolean wasActive = user.getStatus() == UserStatus.ACTIVE;
            user.setRole(roleOpt.get());
            user.setStatus(UserStatus.ACTIVE);
            inviteService.setUsed(user, invitation);

            if (targetRole == RoleType.ADMIN) {
                responseMap.put(true, List.of(wasActive ? botMessages.roleUpdatedAdmin(chatId) : botMessages.congratsAdmin(chatId)));

            } else {
                responseMap.put(true, List.of(wasActive ? botMessages.roleUpdatedSuperAdmin(chatId) : botMessages.congratsSuperAdmin(chatId)));

            }
            return responseMap;
        }

        return responseMap;
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
