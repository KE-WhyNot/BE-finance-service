package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "관심종목 응답")
public record InterestStockResponse(
    @Schema(description = "관심종목 ID", example = "1")
    Long interestStockId,

    @Schema(description = "사용자 ID", example = "1234567890")
    String userId,

    @Schema(description = "종목코드", example = "005930")
    String stockId,

    @Schema(description = "종목명", example = "삼성전자")
    String stockName,

    @Schema(description = "섹터명", example = "기술")
    String sectorName,

    @Schema(description = "등록일시", example = "2025-01-01T10:30:00")
    LocalDateTime createdAt
) {}
