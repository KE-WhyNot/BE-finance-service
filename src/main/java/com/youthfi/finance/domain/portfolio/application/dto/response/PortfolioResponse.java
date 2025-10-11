package com.youthfi.finance.domain.portfolio.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "포트폴리오 응답")
public record PortfolioResponse(
    @Schema(description = "포트폴리오 ID", example = "1")
    Long portfolioId,

    @Schema(description = "사용자 ID", example = "user123")
    String userId,

    List<RecommendedStock> recommendedStocks,

    @Schema(description = "예적금 추천 비율", example = "30.0")
    BigDecimal allocationSavings,

    @Schema(description = "생성일시", example = "2025-01-01T10:30:00")
    LocalDateTime createdAt,

    @Schema(description = "수정일시", example = "2025-01-01T15:45:00")
    LocalDateTime updatedAt
) {
    public record RecommendedStock(
        String stockId,
        String stockName,
        BigDecimal allocationPct,
        String sectorName,
        String reason
    ) {}
}
