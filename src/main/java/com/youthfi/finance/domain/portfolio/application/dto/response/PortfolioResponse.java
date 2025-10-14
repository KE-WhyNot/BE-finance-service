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

    @Schema(description = "추천 주식 목록")
    List<RecommendedStock> recommendedStocks,

    @Schema(description = "예적금 추천 비율", example = "70.0")
    BigDecimal allocationSavings,

    @Schema(description = "최고 예상 가치", example = "15000000.00")
    BigDecimal highestValue,

    @Schema(description = "최저 예상 가치", example = "8000000.00")
    BigDecimal lowestValue,

    @Schema(description = "생성일시", example = "2025-01-01T10:30:00")
    LocalDateTime createdAt,

    @Schema(description = "수정일시", example = "2025-01-01T15:45:00")
    LocalDateTime updatedAt
) {
    @Schema(description = "추천 주식 정보")
    public record RecommendedStock(
        @Schema(description = "주식 ID", example = "005930")
        String stockId,
        
        @Schema(description = "주식명", example = "삼성전자")
        String stockName,
        
        @Schema(description = "배분 비율", example = "30.0")
        BigDecimal allocationPct,
        
        @Schema(description = "섹터명", example = "전기·전자")
        String sectorName,
        
        @Schema(description = "추천 이유", example = "안정적인 성장세와 높은 수익성")
        String reason
    ) {}
}
