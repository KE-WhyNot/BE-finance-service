package com.youthfi.finance.domain.ai.ui;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youthfi.finance.domain.ai.application.dto.request.ChatRequest;
import com.youthfi.finance.domain.ai.application.dto.response.ChatResponse;
import com.youthfi.finance.domain.ai.application.usecase.ChatUseCase;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.security.SecurityUtils;
import com.youthfi.finance.global.swagger.BaseApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "AI 챗봇", description = "AI 챗봇과의 대화 관련 API")
@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "X-User-Id")
public class AiChatController implements BaseApi {

    private final ChatUseCase chatUseCase;

    /**
     * AI 챗봇과 채팅
     */
    @Operation(summary = "AI 챗봇 채팅", description = "AI 챗봇과 대화를 진행합니다.")
    @PostMapping
    public BaseResponse<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("AI 채팅 요청 수신: message={}, sessionId={}", request.message(), request.session_id());
        try {
            String userId = SecurityUtils.getCurrentUserId();
            log.info("사용자 ID 추출: {}", userId);
            ChatResponse response = chatUseCase.processChat(request, userId);
            log.info("AI 채팅 응답 생성 완료");
            return BaseResponse.onSuccess(response);
        } catch (Exception e) {
            log.error("AI 채팅 처리 중 오류 발생", e);
            throw e;
        }
    }

    /**
     * 대화 기록 조회
     */
    @Operation(summary = "대화 기록 조회", description = "특정 세션의 대화 기록을 조회합니다.")
    @GetMapping("/history/{sessionId}")
    public BaseResponse<Map<String, Object>> getConversationHistory(@PathVariable String sessionId) {
        Map<String, Object> history = chatUseCase.getConversationHistory(sessionId);
        return BaseResponse.onSuccess(history);
    }

    /**
     * 대화 기록 초기화
     */
    @Operation(summary = "대화 기록 초기화", description = "특정 세션의 대화 기록을 초기화합니다.")
    @DeleteMapping("/history/{sessionId}")
    public BaseResponse<Map<String, Object>> clearConversationHistory(@PathVariable String sessionId) {
        Map<String, Object> result = chatUseCase.clearConversationHistory(sessionId);
        return BaseResponse.onSuccess(result);
    }
}
