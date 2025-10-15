package com.youthfi.finance.domain.ai.domain.entity;

import java.time.LocalDateTime;

import com.youthfi.finance.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "session_uuid", nullable = false, unique = true)
    private String sessionUuid;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Builder
    public ChatSession(String userId, String sessionUuid) {
        this.userId = userId;
        this.sessionUuid = sessionUuid;
        this.lastActivityAt = LocalDateTime.now();
        this.isActive = true;
    }

    public void updateLastActivity() {
        this.lastActivityAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
        this.lastActivityAt = LocalDateTime.now();
    }
}
