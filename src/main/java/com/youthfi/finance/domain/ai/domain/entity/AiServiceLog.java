package com.youthfi.finance.domain.ai.domain.entity;

import java.time.LocalDateTime;

import com.youthfi.finance.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_service_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiServiceLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "userId")
    private String userId;

    @Column(name = "session_id")
    private String sessionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private RequestType requestType;

    @Column(name = "request_message", columnDefinition = "TEXT")
    private String requestMessage;

    @Column(name = "response_message", columnDefinition = "TEXT")
    private String responseMessage;

    @Column(name = "processing_time_ms")
    private Long processingTimeMs;

    @Column(name = "is_success")
    private Boolean isSuccess;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Builder
    public AiServiceLog(String userId, String sessionId, ServiceType serviceType, 
                       RequestType requestType, String requestMessage, String responseMessage,
                       Long processingTimeMs, Boolean isSuccess, String errorMessage) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.serviceType = serviceType;
        this.requestType = requestType;
        this.requestMessage = requestMessage;
        this.responseMessage = responseMessage;
        this.processingTimeMs = processingTimeMs;
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
        this.requestedAt = LocalDateTime.now();
    }

    public enum ServiceType {
        CHATBOT, PORTFOLIO_RECOMMENDATION, KNOWLEDGE_BASE
    }

    public enum RequestType {
        CHAT_REQUEST, PORTFOLIO_REQUEST, KNOWLEDGE_UPDATE, METRICS_REQUEST
    }
}
