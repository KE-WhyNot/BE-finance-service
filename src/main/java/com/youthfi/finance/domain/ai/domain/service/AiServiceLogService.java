package com.youthfi.finance.domain.ai.domain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.youthfi.finance.domain.ai.domain.entity.AiServiceLog;
import com.youthfi.finance.domain.ai.domain.repository.AiServiceLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiServiceLogService {

    private final AiServiceLogRepository aiServiceLogRepository;

    /**
     * AI 서비스 로그 저장
     */
    public AiServiceLog saveLog(AiServiceLog aiServiceLog) {
        return aiServiceLogRepository.save(aiServiceLog);
    }

    /**
     * 사용자별 AI 서비스 로그 조회
     */
    public List<AiServiceLog> getLogsByUserId(String userId) {
        return aiServiceLogRepository.findByUserIdOrderByRequestedAtDesc(userId);
    }

    /**
     * 세션별 AI 서비스 로그 조회
     */
    public List<AiServiceLog> getLogsBySessionId(String sessionId) {
        return aiServiceLogRepository.findBySessionIdOrderByRequestedAtDesc(sessionId);
    }

    /**
     * 사용자별 서비스 타입별 로그 조회
     */
    public List<AiServiceLog> getLogsByUserIdAndServiceType(String userId, AiServiceLog.ServiceType serviceType) {
        return aiServiceLogRepository.findByUserIdAndServiceTypeOrderByRequestedAtDesc(userId, serviceType);
    }

    /**
     * 기간별 AI 서비스 로그 조회
     */
    public List<AiServiceLog> getLogsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return aiServiceLogRepository.findByRequestedAtBetweenOrderByRequestedAtDesc(startTime, endTime);
    }

    /**
     * 사용자별 성공 요청 수 조회
     */
    public long getSuccessCountByUserId(String userId) {
        return aiServiceLogRepository.countSuccessfulRequestsByUserId(userId);
    }

    /**
     * 사용자별 실패 요청 수 조회
     */
    public long getFailureCountByUserId(String userId) {
        return aiServiceLogRepository.countFailedRequestsByUserId(userId);
    }

    /**
     * 사용자별 평균 처리 시간 조회
     */
    public Double getAverageProcessingTimeByUserId(String userId) {
        return aiServiceLogRepository.getAverageProcessingTimeByUserId(userId);
    }

    /**
     * 사용자별 AI 서비스 로그 페이징 조회
     */
    public Page<AiServiceLog> getLogsByUserId(String userId, Pageable pageable) {
        return aiServiceLogRepository.findByUserId(userId, pageable);
    }

    /**
     * AI 서비스 성능 메트릭 조회
     */
    public PerformanceMetrics getPerformanceMetrics(String userId) {
        long successCount = getSuccessCountByUserId(userId);
        long failureCount = getFailureCountByUserId(userId);
        Double avgProcessingTime = getAverageProcessingTimeByUserId(userId);
        
        return PerformanceMetrics.builder()
                .successCount(successCount)
                .failureCount(failureCount)
                .totalRequests(successCount + failureCount)
                .successRate(successCount + failureCount > 0 ? (double) successCount / (successCount + failureCount) : 0.0)
                .averageProcessingTimeMs(avgProcessingTime != null ? avgProcessingTime : 0.0)
                .build();
    }
}
