package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "실시간 주식 WebSocket 메시지 포맷")
@Getter
@Builder
@AllArgsConstructor
public class StockWebSocketResponse {
    @Schema(description = "종목코드")
    private final String symbol;
    @Schema(description = "매도호가 1~4")
    private final List<Integer> askPrices;
    @Schema(description = "매수호가 1~4")
    private final List<Integer> bidPrices;
    @Schema(description = "매도호가 잔량 1~4")
    private final List<Integer> askQtys;
    @Schema(description = "매수호가 잔량 1~4")
    private final List<Integer> bidQtys;
    @Schema(description = "현재가")
    private final Integer stckPrpr;
    @Schema(description = "전일대비")
    private final Integer prdyVrss;
    @Schema(description = "전일대비율")
    private final Double prdyCtrt;
    @Schema(description = "최저가")
    private final Integer stckLwpr;
    @Schema(description = "최고가")
    private final Integer stckHgpr;
    @Schema(description = "타임스탬프")
    private final String timestamp;
}


