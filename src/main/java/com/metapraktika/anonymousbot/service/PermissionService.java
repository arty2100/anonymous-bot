package com.metapraktika.anonymousbot.service;


import com.metapraktika.anonymousbot.entity.User;
import com.metapraktika.anonymousbot.enums.RoleType;
import com.metapraktika.anonymousbot.enums.UserStatus;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public boolean canUseBot(User user){
        return user != null &&  user.getStatus() == UserStatus.ACTIVE;
    }

    private boolean isAdmin(User user) {
        RoleType role = user.getRole().getName();
        return role == RoleType.ADMIN || role == RoleType.SUPER_ADMIN;
    }
}
