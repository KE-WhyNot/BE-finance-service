package com.youthfi.finance.domain.ai.domain.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PerformanceMetrics {
    private final long successCount;
    private final long failureCount;
    private final long totalRequests;
    private final double successRate;
    private final double averageProcessingTimeMs;

    @Builder
    public PerformanceMetrics(long successCount, long failureCount, long totalRequests, 
                            double successRate, double averageProcessingTimeMs) {
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.totalRequests = totalRequests;
        this.successRate = successRate;
        this.averageProcessingTimeMs = averageProcessingTimeMs;
    }
}
