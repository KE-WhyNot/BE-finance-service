package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "사용자 보유 주식 최소 정보 응답")
public record UserHoldingResponse(
    @Schema(description = "사용자 보유 주식 ID", example = "1")
    Long userStockId,

    @Schema(description = "종목코드", example = "005930")
    String stockId,

    @Schema(description = "종목명", example = "삼성전자")
    String stockName,

    @Schema(description = "보유 수량", example = "10")
    Long holdingQuantity,

    @Schema(description = "평균 매입가", example = "70000.00")
    BigDecimal avgPrice,

    @Schema(description = "보유 등록일시", example = "2025-01-01T00:00:00")
    LocalDateTime createdAt
) {}

