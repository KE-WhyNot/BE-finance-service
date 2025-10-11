package com.youthfi.finance.domain.ai.application.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record AiServiceStatusResponse(
    Boolean isConnected,
    String serviceUrl,
    LocalDateTime lastHealthCheck,
    Map<String, Object> metrics,
    String errorMessage
) {
    public static AiServiceStatusResponse success(String serviceUrl, LocalDateTime lastHealthCheck, 
                                                Map<String, Object> metrics) {
        return new AiServiceStatusResponse(
                true,
                serviceUrl,
                lastHealthCheck,
                metrics,
                null
        );
    }

    public static AiServiceStatusResponse error(String serviceUrl, String errorMessage) {
        return new AiServiceStatusResponse(
                false,
                serviceUrl,
                null,
                null,
                errorMessage
        );
    }
}
