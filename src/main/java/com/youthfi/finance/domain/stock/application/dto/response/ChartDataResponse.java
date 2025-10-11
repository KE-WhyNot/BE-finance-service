package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 차트 데이터 (캔들 리스트 + 메타데이터)
 */
@Schema(description = "차트 데이터 응답")
public record ChartDataResponse(
    @Schema(description = "종목코드", example = "005930")
    String stockCode,

    @Schema(description = "기간 (1d, 1m, 1y, 1min)", example = "1d")
    String period,

    @Schema(description = "범위 (60d, 5y, 10y, today)", example = "60d")
    String range,

    @Schema(description = "캔들 데이터 리스트")
    List<CandleDataResponse> candles,

    @Schema(description = "마지막 업데이트 시간 (ISO)", example = "2025-01-01T10:30:00")
    String lastUpdated
) {
    
    /**
     * 빈 차트 데이터 생성
     */
    public static ChartDataResponse empty(String stockCode, String period, String range) {
        return new ChartDataResponse(
            stockCode,
            period, 
            range,
            List.of(),
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}
