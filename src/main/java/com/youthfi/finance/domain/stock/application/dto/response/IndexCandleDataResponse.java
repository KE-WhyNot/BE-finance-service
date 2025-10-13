package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 지수 캔들 데이터 응답 DTO
 */
@Schema(description = "지수 캔들 데이터")
public record IndexCandleDataResponse(
    @Schema(description = "주식 영업 일자", example = "20251010")
    String date,
    
    @Schema(description = "지수 현재가", example = "2500.00")
    String currentPrice,
    
    @Schema(description = "지수 최고가", example = "2550.00")
    String highPrice,
    
    @Schema(description = "지수 최저가", example = "2480.00")
    String lowPrice,
    
    @Schema(description = "누적 거래량", example = "1000000")
    String volume
) {
    
    /**
     * 팩토리 메서드 - KIS API 응답 데이터로부터 생성
     */
    public static IndexCandleDataResponse of(
            String date,
            String currentPrice,
            String highPrice,
            String lowPrice,
            String volume) {
        return new IndexCandleDataResponse(
            date,
            currentPrice,
            highPrice,
            lowPrice,
            volume
        );
    }
}
