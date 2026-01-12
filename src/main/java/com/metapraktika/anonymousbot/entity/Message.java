package com.metapraktika.anonymousbot.entity;

import com.metapraktika.anonymousbot.enums.MessageStatus;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "text")
    private String text;

    @Column(name = "admin_reply", columnDefinition = "text")
    private String adminReply;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private MessageStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "answered_at")
    private OffsetDateTime answeredAt;

    protected Message() {
    }

    public Message(User user, String text) {
        this.user = user;
        this.text = text;
        this.status = MessageStatus.NEW;
        this.createdAt = OffsetDateTime.now();
    }

    // ===== getters / setters =====

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public String getAdminReply() {
        return adminReply;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getAnsweredAt() {
        return answeredAt;
    }

    public void setAdminReply(String adminReply) {
        this.adminReply = adminReply;
        this.answeredAt = OffsetDateTime.now();
        this.status = MessageStatus.ANSWERED;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}
