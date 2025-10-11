package com.youthfi.finance.domain.ai.application.usecase;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youthfi.finance.domain.ai.application.dto.request.ChatRequest;
import com.youthfi.finance.domain.ai.application.dto.response.ChatResponse;
import com.youthfi.finance.domain.ai.domain.service.ChatSessionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatUseCase {

    private final ChatSessionService chatSessionService;

    /**
     * AI 챗봇과 채팅 처리
     */
    @Transactional
    public ChatResponse processChat(ChatRequest request, String userId) {
        log.info("채팅 처리 시작: userId={}, sessionId={}, message={}", userId, request.session_id(), request.message());
        
        try {
            // 1. 사용자 ID가 포함된 ChatRequest 생성
            ChatRequest chatRequest = new ChatRequest(
                    request.message(),
                    userId,
                    request.session_id()
            );
            log.info("ChatRequest 생성 완료: user_id={}, session_id={}", chatRequest.user_id(), chatRequest.session_id());
            
            // 2. 비즈니스 로직은 Service에 위임
            log.info("ChatSessionService.processChat 호출 시작");
            ChatResponse response = chatSessionService.processChat(chatRequest);
            log.info("ChatSessionService.processChat 호출 완료");
            
            return response;
        } catch (Exception e) {
            log.error("채팅 처리 중 오류 발생: userId={}, sessionId={}", userId, request.session_id(), e);
            throw e;
        }
    }

    /**
     * 대화 기록 조회
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getConversationHistory(String sessionId) {
        log.info("대화 기록 조회: sessionId={}", sessionId);
        return chatSessionService.getConversationHistory(sessionId);
    }

    /**
     * 대화 기록 초기화
     */
    @Transactional
    public Map<String, Object> clearConversationHistory(String sessionId) {
        log.info("대화 기록 초기화: sessionId={}", sessionId);
        return chatSessionService.clearConversationHistory(sessionId);
    }

}
