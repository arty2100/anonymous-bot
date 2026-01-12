package com.metapraktika.anonymousbot.enums;

import java.util.EnumSet;
import java.util.List;

public enum BotCommandDef {

    // USER
    HELP("/help", "список команд", RoleType.USER, RoleType.ADMIN, RoleType.SUPER_ADMIN),

    // ADMIN + SUPER_ADMIN
    INVITE("/invite", "пригласить пользователя", RoleType.ADMIN, RoleType.SUPER_ADMIN),

    // SUPER_ADMIN
   // GRANT_ADMIN("/grant_admin <user_name>", "выдать админа", RoleType.SUPER_ADMIN),
    INVITE_ADMIN("/invite_admin", "пригласить админа", RoleType.SUPER_ADMIN),
    INVITE_SUPER_ADMIN("/invite_super_admin", "пригласить супер админа", RoleType.SUPER_ADMIN);

    private final String command;
    private final String description;
    private final EnumSet<RoleType> allowedRoles;

    BotCommandDef(String command, String description, RoleType... roles) {
        this.command = command;
        this.description = description;
        this.allowedRoles = EnumSet.copyOf(List.of(roles));
    }

    public String command() {
        return command;
    }

    public String description() {
        return description;
    }

    public boolean isAllowedFor(RoleType role) {
        return allowedRoles.contains(role);
    }

}
