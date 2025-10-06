package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "거래 내역 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionResponse {

    @Schema(description = "거래 ID", example = "1")
    private Long executionId;

    @Schema(description = "사용자 ID", example = "1234567890")
    private String userId;

    @Schema(description = "종목코드", example = "005930")
    private String stockId;

    @Schema(description = "종목명", example = "삼성전자")
    private String stockName;

    @Schema(description = "거래 유형", example = "BUY")
    private String executionType;

    @Schema(description = "거래 수량", example = "10")
    private Integer quantity;

    @Schema(description = "거래 가격", example = "70000.00")
    private BigDecimal price;

    @Schema(description = "총 거래 금액", example = "700000.00")
    private BigDecimal totalAmount;

    @Schema(description = "거래일시", example = "2025-01-01T10:30:00")
    private LocalDateTime executedAt;
}
