package com.youthfi.finance.domain.user.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "사용자 정보 응답")
public record UserResponse(
    @Schema(description = "사용자 ID", example = "1234567890")
    String userId,

    @Schema(description = "현재 잔고", example = "10000000.00")
    BigDecimal balance,

    @Schema(description = "생성일시", example = "2025-01-01T00:00:00")
    LocalDateTime createdAt,

    @Schema(description = "수정일시", example = "2025-01-01T12:00:00")
    LocalDateTime updatedAt
) {}
