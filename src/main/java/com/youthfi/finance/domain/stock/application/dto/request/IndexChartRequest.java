package com.youthfi.finance.domain.stock.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 지수 차트 요청 DTO
 */
@Schema(description = "지수 차트 요청")
public record IndexChartRequest(
    @Schema(description = "지수 코드 (0001: 코스피, 1001: 코스닥)", example = "0001")
    String indexCode,
    
    @Schema(description = "기간 분류코드 (D: 일봉, W: 주봉, M: 월봉)", example = "D")
    String period,
    
    @Schema(description = "조회 기간 (30d, 30w, 30m)", example = "30d")
    String range
) {
    
    /**
     * 유효한 기간인지 확인
     */
    public boolean isValidPeriod() {
        return "D".equals(period) || "W".equals(period) || "M".equals(period);
    }
    
    /**
     * 유효한 범위인지 확인
     */
    public boolean isValidRange() {
        return "30d".equals(range) || "30w".equals(range) || "30m".equals(range);
    }
    
    /**
     * 유효한 지수 코드인지 확인
     */
    public boolean isValidIndexCode() {
        return "0001".equals(indexCode) || "1001".equals(indexCode);
    }
    
    /**
     * 전체 요청이 유효한지 확인
     */
    public boolean isValid() {
        return isValidIndexCode() && isValidPeriod() && isValidRange();
    }
}
