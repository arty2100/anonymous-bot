package com.metapraktika.anonymousbot.service;

import com.metapraktika.anonymousbot.entity.Invite;
import com.metapraktika.anonymousbot.entity.User;
import com.metapraktika.anonymousbot.enums.RoleType;
import com.metapraktika.anonymousbot.repository.InviteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class InviteService {

    private final InviteRepository inviteRepository;

    public InviteService(InviteRepository inviteRepository) {
        this.inviteRepository = inviteRepository;
    }


    public Invite getInvitation(String inviteCode) {

        Optional<Invite> invitation = inviteRepository.findByInvitation(inviteCode);

        return invitation.orElse(null);
    }

    @Transactional
    public void setUsed(User user, Invite invitation) {
        invitation.setUsed(true);
        invitation.setUsedBy(user);
        invitation.setUsedAt(LocalDateTime.now());
        inviteRepository.save(invitation);
    }

    public Invite createInvite(User user, RoleType targetRole) {

        Invite invite = new Invite();
        invite.setInvitation(UUID.randomUUID().toString());
        invite.setTargetRole(targetRole);
        invite.setCreatedBy(user);
        invite.setExpiresAt(LocalDateTime.now().plusDays(1));

        return inviteRepository.save(invite);

    }
}
