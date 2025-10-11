package com.youthfi.finance.domain.stock.application.dto.request;

/**
 * 차트 조회 요청
 */
public record ChartRequest(
    String stockCode,   // 종목코드
    String period,      // 기간 (1d, 1m, 1y, 1min)
    String range        // 범위 (60d, 5y, 10y, today)
) {
    
    /**
     * 유효한 기간인지 검증
     */
    public boolean isValidPeriod() {
        return period != null && 
               (period.equals("1d") || period.equals("1m") || 
                period.equals("1y") || period.equals("1min"));
    }
    
    /**
     * 유효한 범위인지 검증
     */
    public boolean isValidRange() {
        return range != null && 
               (range.equals("60d") || range.equals("5y") || 
                range.equals("10y") || range.equals("today"));
    }
}
