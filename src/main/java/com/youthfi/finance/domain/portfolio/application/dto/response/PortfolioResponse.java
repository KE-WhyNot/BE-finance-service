package com.youthfi.finance.domain.portfolio.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "포트폴리오 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponse {

    @Schema(description = "포트폴리오 ID", example = "1")
    private Long portfolioId;

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    private List<RecommendedStock> recommendedStocks;
    // 추천 주식 정보
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendedStock {
        private String stockId;        // 종목코드
        private String stockName;      // 종목명
        private BigDecimal allocationPct; // 배분 비율
        private String sectorName;     // 섹터명
        private String reason;         // 추천 이유
    }


    @Schema(description = "생성일시", example = "2025-01-01T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-01-01T15:45:00")
    private LocalDateTime updatedAt;
}
