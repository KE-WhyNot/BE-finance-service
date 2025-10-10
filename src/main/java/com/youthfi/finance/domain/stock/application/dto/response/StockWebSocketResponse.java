package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "실시간 주식 WebSocket 메시지 포맷")
public record StockWebSocketResponse(
    @Schema(description = "종목코드")
    String symbol,

    @Schema(description = "매도호가 1~4")
    List<Integer> askPrices,

    @Schema(description = "매수호가 1~4")
    List<Integer> bidPrices,

    @Schema(description = "매도호가 잔량 1~4")
    List<Integer> askQtys,

    @Schema(description = "매수호가 잔량 1~4")
    List<Integer> bidQtys,

    @Schema(description = "현재가")
    Integer stckPrpr,

    @Schema(description = "전일대비")
    Integer prdyVrss,

    @Schema(description = "전일대비율")
    Double prdyCtrt,

    @Schema(description = "최저가")
    Integer stckLwpr,

    @Schema(description = "최고가")
    Integer stckHgpr,
    
    @Schema(description = "타임스탬프")
    String timestamp
) {}


