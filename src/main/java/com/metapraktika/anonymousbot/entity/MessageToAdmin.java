package com.metapraktika.anonymousbot.entity;

import com.metapraktika.anonymousbot.enums.AdminMessageStatus;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "message_to_admin",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"message_id", "admin_id"})
        }
)
public class MessageToAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private AdminMessageStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;

    protected MessageToAdmin() {
    }

    public MessageToAdmin(Message message, User admin) {
        this.message = message;
        this.admin = admin;
        this.status = AdminMessageStatus.PENDING;
        this.createdAt = OffsetDateTime.now();
    }

    // ===== domain methods =====

    public void markSent() {
        this.status = AdminMessageStatus.SENT;
        this.sentAt = OffsetDateTime.now();
    }

    public void markFailed() {
        this.status = AdminMessageStatus.FAILED;
    }

    // ===== getters =====

    public Long getId() {
        return id;
    }

    public Message getMessage() {
        return message;
    }

    public User getAdmin() {
        return admin;
    }

    public AdminMessageStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getSentAt() {
        return sentAt;
    }
}
