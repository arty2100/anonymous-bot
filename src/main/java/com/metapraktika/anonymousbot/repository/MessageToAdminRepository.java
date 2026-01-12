package com.metapraktika.anonymousbot.repository;

import com.metapraktika.anonymousbot.entity.MessageToAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageToAdminRepository extends JpaRepository<MessageToAdmin, Long> {
}
