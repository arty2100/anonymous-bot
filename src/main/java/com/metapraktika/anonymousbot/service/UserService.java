package com.metapraktika.anonymousbot.service;



import com.metapraktika.anonymousbot.entity.Role;
import com.metapraktika.anonymousbot.entity.User;
import com.metapraktika.anonymousbot.enums.RoleType;
import com.metapraktika.anonymousbot.enums.UserStatus;
import com.metapraktika.anonymousbot.repository.RoleRepository;
import com.metapraktika.anonymousbot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRedisService userRedisService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserRedisService userRedisService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRedisService = userRedisService;
    }

    public User getOrCreate(Long telegramId, String username, String firstName, String userLastName) {
        return userRepository.findByTelegramId(telegramId)
                .orElseGet(() -> {
                    Optional<Role> roleOptional = roleRepository.findByName(RoleType.USER);

                    if (roleOptional.isPresent()) {
                        Role role = roleOptional.get();
                        User newUser = new User(telegramId, username, firstName, userLastName, role, UserStatus.NEW);
                        return userRepository.save(newUser);
                    } else {
                        throw new RuntimeException("Role not found");
                    }
                });
    }

    public void updateLastKeyboardMessageId(Long telegramId, Integer messageId) {
        userRedisService.saveLastKeyboardMessageId(telegramId, messageId);
    }

    public void clearLastKeyboardMessageId(Long telegramId) {
        userRedisService.deleteLastKeyboardMessageId(telegramId);
    }

    public Integer getLastKeyboardMessageId(Long telegramId) {
        return userRedisService.getLastKeyboardMessageId(telegramId).orElse(null);
    }
}

