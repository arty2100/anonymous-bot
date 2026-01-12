package com.metapraktika.anonymousbot.repository;

import com.metapraktika.anonymousbot.entity.User;
import com.metapraktika.anonymousbot.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByTelegramId(Long telegramId);

    List<User> findAllByRoleNameIn(Collection<RoleType> roles);
}