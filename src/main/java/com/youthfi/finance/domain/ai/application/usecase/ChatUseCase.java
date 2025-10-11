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
        log.info("채팅 처리 시작: userId={}, sessionId={}", userId, request.session_id());
        
        // 1. 사용자 ID가 포함된 ChatRequest 생성
        ChatRequest chatRequest = new ChatRequest(
                request.message(),
                userId,
                request.session_id()
        );
        
        // 2. 비즈니스 로직은 Service에 위임
        return chatSessionService.processChat(chatRequest);
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
