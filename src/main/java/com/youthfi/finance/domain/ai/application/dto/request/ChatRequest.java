package com.youthfi.finance.domain.ai.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChatRequest(
    @NotBlank(message = "메시지는 필수입니다.")
    @Size(max = 2000, message = "메시지는 2000자를 초과할 수 없습니다.")
    String message,
    
    @NotNull(message = "사용자 ID는 필수입니다.")
    String user_id,
    
    @NotBlank(message = "세션 ID는 필수입니다.")
    String session_id
) {
    public ChatRequest {
        if (message != null) {
            message = message.trim();
        }
    }
}
