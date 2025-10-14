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
        log.info("=== AiChatController.chat 시작 ===");
        log.info("받은 요청 데이터: message={}, sessionId={}", request.message(), request.session_id());
        
        try {
            // 사용자 ID 추출
            log.debug("사용자 ID 추출 시작");
            String userId = SecurityUtils.getCurrentUserId();
            log.info("추출된 사용자 ID: {}", userId);
            
            // UseCase 호출
            log.info("ChatUseCase.processChat 호출 시작");
            ChatResponse response = chatUseCase.processChat(request, userId);
            log.info("ChatUseCase.processChat 호출 완료");
            
            if (response != null) {
                log.info("Controller에서 받은 응답: replyText={}, success={}, errorMessage={}", 
                        response.replyText(), response.success(), response.errorMessage());
            } else {
                log.error("Controller에서 받은 응답이 null입니다!");
            }
            
            // BaseResponse 생성
            log.debug("BaseResponse 생성 시작");
            BaseResponse<ChatResponse> baseResponse = BaseResponse.onSuccess(response);
            log.info("BaseResponse 생성 완료: code={}, message={}, result 존재={}", 
                    baseResponse.getCode(), baseResponse.getMessage(), baseResponse.getResult() != null);
            
            log.info("=== AiChatController.chat 성공 완료 ===");
            return baseResponse;
            
        } catch (Exception e) {
            log.error("=== AiChatController.chat 중 예외 발생 ===");
            log.error("예외 타입: {}", e.getClass().getSimpleName());
            log.error("예외 메시지: {}", e.getMessage());
            log.error("예외 스택 트레이스:", e);
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
