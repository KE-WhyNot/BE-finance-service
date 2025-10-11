package com.youthfi.finance.domain.ai.ui;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youthfi.finance.domain.ai.application.dto.response.AiServiceStatusResponse;
import com.youthfi.finance.domain.ai.application.usecase.AiServiceManagementUseCase;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.security.SecurityUtils;
import com.youthfi.finance.global.swagger.BaseApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "AI 서비스 관리", description = "AI 서비스 상태 및 관리 관련 API")
@RestController
@RequestMapping("/api/ai/management")
@RequiredArgsConstructor
@Slf4j
public class AiManagementController implements BaseApi {

    private final AiServiceManagementUseCase aiServiceManagementUseCase;

    /**
     * AI 서비스 상태 조회
     */
    @Operation(summary = "AI 서비스 상태 조회", description = "AI 서비스들의 연결 상태를 조회합니다.")
    @GetMapping("/status")
    public BaseResponse<AiServiceStatusResponse> getServiceStatus() {
        AiServiceStatusResponse status = aiServiceManagementUseCase.getServiceStatus();
        return BaseResponse.onSuccess(status);
    }

    /**
     * 성능 메트릭 조회
     */
    @Operation(summary = "성능 메트릭 조회", description = "AI 서비스의 성능 메트릭을 조회합니다.")
    @GetMapping("/metrics")
    public BaseResponse<Map<String, Object>> getPerformanceMetrics() {
        Map<String, Object> metrics = aiServiceManagementUseCase.getPerformanceMetrics();
        return BaseResponse.onSuccess(metrics);
    }

    /**
     * 지식 베이스 통계 조회
     */
    @Operation(summary = "지식 베이스 통계 조회", description = "AI 지식 베이스의 통계 정보를 조회합니다.")
    @GetMapping("/knowledge-base/stats")
    public BaseResponse<Map<String, Object>> getKnowledgeBaseStats() {
        Map<String, Object> stats = aiServiceManagementUseCase.getKnowledgeBaseStats();
        return BaseResponse.onSuccess(stats);
    }

    /**
     * 사용자별 AI 서비스 로그 조회
     */
    @Operation(summary = "사용자 AI 서비스 로그 조회", description = "특정 사용자의 AI 서비스 사용 로그를 조회합니다.")
    @GetMapping("/logs/my")
    public BaseResponse<Map<String, Object>> getMyServiceLogs() {
        String userId = SecurityUtils.getCurrentUserId();
        Map<String, Object> logs = aiServiceManagementUseCase.getUserServiceLogs(userId);
        return BaseResponse.onSuccess(logs);
    }
}
