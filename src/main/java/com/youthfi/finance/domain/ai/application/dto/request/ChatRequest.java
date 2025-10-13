package com.youthfi.finance.domain.ai.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(
    @NotBlank(message = "메시지는 필수입니다.")
    @Size(max = 2000, message = "메시지는 2000자를 초과할 수 없습니다.")
    String message,
    
    String user_id,  // X-User-Id 헤더에서 가져오므로 선택적
    
    @NotBlank(message = "세션 ID는 필수입니다.")
    String session_id
) {
    public ChatRequest {
        if (message != null) {
            message = message.trim();
        }
    }
}
