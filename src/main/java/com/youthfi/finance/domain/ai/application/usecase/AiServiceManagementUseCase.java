package com.youthfi.finance.domain.ai.application.usecase;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youthfi.finance.domain.ai.application.dto.response.AiServiceStatusResponse;
import com.youthfi.finance.domain.ai.domain.service.AiServiceLogService;
import com.youthfi.finance.domain.ai.domain.service.PerformanceMetrics;
import com.youthfi.finance.domain.ai.infra.AiChatbotApiClient;
import com.youthfi.finance.domain.portfolio.infra.LLMApiClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiServiceManagementUseCase {

    private final AiChatbotApiClient aiChatbotApiClient;
    private final LLMApiClient llmApiClient;
    private final AiServiceLogService aiServiceLogService;

    /**
     * AI 서비스 상태 조회
     */
    @Transactional(readOnly = true)
    public AiServiceStatusResponse getServiceStatus() {
        log.info("AI 서비스 상태 조회 시작");
        
        boolean chatbotConnected = aiChatbotApiClient.checkConnection();
        boolean portfolioConnected = llmApiClient.checkConnection();
        
        Map<String, Object> metrics = aiChatbotApiClient.getPerformanceMetrics();
        
        if (chatbotConnected && portfolioConnected) {
            return AiServiceStatusResponse.success(
                "AI 서비스 정상", 
                LocalDateTime.now(), 
                metrics
            );
        } else {
            return AiServiceStatusResponse.error(
                "AI 서비스", 
                "일부 AI 서비스에 연결할 수 없습니다."
            );
        }
    }

    /**
     * 성능 메트릭 조회
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getPerformanceMetrics() {
        log.info("성능 메트릭 조회 시작");
        return aiChatbotApiClient.getPerformanceMetrics();
    }

    /**
     * 지식 베이스 통계 조회
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getKnowledgeBaseStats() {
        log.info("지식 베이스 통계 조회 시작");
        return aiChatbotApiClient.getKnowledgeBaseStats();
    }

    /**
     * 사용자별 AI 서비스 로그 조회
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getUserServiceLogs(String userId) {
        log.info("사용자 AI 서비스 로그 조회: userId={}", userId);
        
        PerformanceMetrics performanceMetrics = aiServiceLogService.getPerformanceMetrics(userId);
        
        return Map.of(
            "userId", userId,
            "successCount", performanceMetrics.getSuccessCount(),
            "failureCount", performanceMetrics.getFailureCount(),
            "totalRequests", performanceMetrics.getTotalRequests(),
            "successRate", performanceMetrics.getSuccessRate(),
            "averageProcessingTimeMs", performanceMetrics.getAverageProcessingTimeMs()
        );
    }
}
