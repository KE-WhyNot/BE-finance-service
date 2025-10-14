package com.youthfi.finance.domain.ai.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.youthfi.finance.domain.ai.application.dto.request.ChatRequest;
import com.youthfi.finance.domain.ai.application.dto.response.ChatResponse;
import com.youthfi.finance.domain.ai.domain.entity.AiServiceLog;
import com.youthfi.finance.domain.ai.domain.entity.ChatSession;
import com.youthfi.finance.domain.ai.domain.repository.ChatSessionRepository;
import com.youthfi.finance.domain.ai.infra.AiChatbotApiClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final AiChatbotApiClient aiChatbotApiClient;
    private final AiServiceLogService aiServiceLogService;

    /**
     * 새로운 채팅 세션 생성
     */
    public ChatSession createSession(String userId) {
        String sessionUuid = UUID.randomUUID().toString();
        ChatSession session = ChatSession.builder()
                .userId(userId)
                .sessionUuid(sessionUuid)
                .build();
        
        return chatSessionRepository.save(session);
    }

    /**
     * 세션 ID로 세션 조회
     */
    public Optional<ChatSession> getSessionByUuid(String sessionUuid) {
        return chatSessionRepository.findBySessionUuid(sessionUuid);
    }

    /**
     * 사용자의 활성 세션 목록 조회
     */
    public List<ChatSession> getActiveSessionsByUserId(String userId) {
        return chatSessionRepository.findActiveSessionsByUserIdOrderByLastActivity(userId);
    }

    /**
     * 세션 활동 시간 업데이트
     */
    public void updateSessionActivity(String sessionUuid) {
        chatSessionRepository.findBySessionUuid(sessionUuid)
                .ifPresent(ChatSession::updateLastActivity);
    }

    /**
     * 세션 비활성화
     */
    public void deactivateSession(String sessionUuid) {
        chatSessionRepository.findBySessionUuid(sessionUuid)
                .ifPresent(ChatSession::deactivate);
    }

    /**
     * 세션 활성화
     */
    public void activateSession(String sessionUuid) {
        chatSessionRepository.findBySessionUuid(sessionUuid)
                .ifPresent(ChatSession::activate);
    }

    /**
     * 비활성 세션 정리 (30일 이상 비활성)
     */
    public void cleanupInactiveSessions() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(30);
        List<ChatSession> inactiveSessions = chatSessionRepository.findInactiveSessions(cutoffTime);
        
        for (ChatSession session : inactiveSessions) {
            session.deactivate();
        }
        
        log.info("비활성 세션 {}개 정리 완료", inactiveSessions.size());
    }

    /**
     * 사용자의 활성 세션 수 조회
     */
    public long getActiveSessionCount(String userId) {
        return chatSessionRepository.countByUserIdAndIsActiveTrue(userId);
    }

    /**
     * AI 챗봇과 채팅 처리 (비즈니스 로직)
     */
    public ChatResponse processChat(ChatRequest request) {
        log.info("=== ChatSessionService.processChat 시작 ===");
        log.info("요청 데이터: userId={}, sessionId={}, message={}", 
                request.user_id(), request.session_id(), request.message());
        
        try {
            // 1. 세션 활동 시간 업데이트
            if (request.session_id() != null) {
                log.debug("세션 활동 시간 업데이트 시작: sessionId={}", request.session_id());
                updateSessionActivity(request.session_id());
                log.debug("세션 활동 시간 업데이트 완료");
            } else {
                log.debug("세션 ID가 null이므로 세션 활동 시간 업데이트 건너뜀");
            }
            
            // 2. AI 챗봇 API 호출
            log.info("AI 챗봇 API 호출 시작");
            ChatResponse response = aiChatbotApiClient.sendChatRequest(request);
            log.info("AI 챗봇 API 호출 완료");
            
            if (response != null) {
                log.info("받은 응답 데이터: replyText={}, success={}, errorMessage={}", 
                        response.replyText(), response.success(), response.errorMessage());
            } else {
                log.error("AI 챗봇 API 응답이 null입니다!");
            }
            
            // 3. 로그 저장
            log.debug("AI 서비스 로그 저장 시작");
            try {
                aiServiceLogService.saveLog(createChatLog(request, response));
                log.debug("AI 서비스 로그 저장 완료");
            } catch (Exception logException) {
                log.error("AI 서비스 로그 저장 중 오류 발생: {}", logException.getMessage());
                // 로그 저장 실패는 전체 프로세스를 중단시키지 않음
            }
            
            log.info("=== ChatSessionService.processChat 성공 완료 ===");
            return response;
            
        } catch (Exception e) {
            log.error("=== ChatSessionService.processChat 중 예외 발생 ===");
            log.error("예외 타입: {}", e.getClass().getSimpleName());
            log.error("예외 메시지: {}", e.getMessage());
            log.error("예외 스택 트레이스:", e);
            throw e;
        }
    }

    /**
     * 대화 기록 조회 (비즈니스 로직)
     */
    public Map<String, Object> getConversationHistory(String sessionId) {
        log.info("대화 기록 조회: sessionId={}", sessionId);
        return aiChatbotApiClient.getConversationHistory(sessionId);
    }

    /**
     * 대화 기록 초기화 (비즈니스 로직)
     */
    public Map<String, Object> clearConversationHistory(String sessionId) {
        log.info("대화 기록 초기화: sessionId={}", sessionId);
        return aiChatbotApiClient.clearConversationHistory(sessionId);
    }

    /**
     * 채팅 로그 생성
     */
    private AiServiceLog createChatLog(ChatRequest request, ChatResponse response) {
        return AiServiceLog.builder()
                .userId(request.user_id())
                .sessionId(request.session_id())
                .serviceType(AiServiceLog.ServiceType.CHATBOT)
                .requestType(AiServiceLog.RequestType.CHAT_REQUEST)
                .requestMessage(request.message())
                .responseMessage(response != null ? response.replyText() : "응답 없음")
                .processingTimeMs(0L) // 실제로는 측정된 시간을 넣어야 함
                .isSuccess(response != null ? response.success() : false)
                .errorMessage(response != null ? response.errorMessage() : "응답이 null입니다")
                .build();
    }
}
