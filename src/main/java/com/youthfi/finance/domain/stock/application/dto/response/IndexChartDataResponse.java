package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 지수 차트 데이터 응답 DTO
 */
@Schema(description = "지수 차트 데이터 응답")
public record IndexChartDataResponse(
    @Schema(description = "지수 코드", example = "0001")
    String indexCode,

    @Schema(description = "기간 (D, W, M)", example = "D")
    String period,

    @Schema(description = "범위 (30d, 30w, 30m)", example = "30d")
    String range,

    @Schema(description = "지수 캔들 데이터 리스트")
    List<IndexCandleDataResponse> candles,

    @Schema(description = "마지막 업데이트 시간 (ISO)", example = "2025-01-01T10:30:00")
    String lastUpdated
) {
    
    /**
     * 빈 차트 데이터 생성
     */
    public static IndexChartDataResponse empty(String indexCode, String period, String range) {
        return new IndexChartDataResponse(
            indexCode,
            period, 
            range,
            List.of(),
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}
