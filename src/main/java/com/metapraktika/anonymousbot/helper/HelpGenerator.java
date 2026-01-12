package com.metapraktika.anonymousbot.helper;


import com.metapraktika.anonymousbot.enums.BotCommandDef;
import com.metapraktika.anonymousbot.enums.RoleType;

public class HelpGenerator {

    public static String generate(RoleType userRole) {
        StringBuilder sb = new StringBuilder("Доступные команды:\n\n");

        for (BotCommandDef cmd : BotCommandDef.values()) {
            if (cmd.isAllowedFor(userRole)) {
                sb.append(cmd.command())
                        .append(" - ")
                        .append(cmd.description())
                        .append("\n");
            }
        }

        return sb.toString();
    }
}
