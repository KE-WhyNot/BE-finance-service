package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "거래 내역 응답")
public record ExecutionResponse(
    @Schema(description = "거래 ID", example = "1")
    Long executionId,

    @Schema(description = "사용자 ID", example = "1234567890")
    String userId,

    @Schema(description = "종목코드", example = "005930")
    String stockId,

    @Schema(description = "종목명", example = "삼성전자")
    String stockName,

    @Schema(description = "거래 유형", example = "BUY")
    String executionType,

    @Schema(description = "거래 수량", example = "10")
    Integer quantity,

    @Schema(description = "거래 가격", example = "70000.00")
    BigDecimal price,

    @Schema(description = "총 거래 금액", example = "700000.00")
    BigDecimal totalAmount,

    @Schema(description = "거래일시", example = "2025-01-01T10:30:00")
    LocalDateTime executedAt,

    @Schema(description = "거래 후 잔고", example = "5000000.00")
    BigDecimal userBalance
) {}
