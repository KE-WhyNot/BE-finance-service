package com.youthfi.finance.domain.stock.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "현재가 기반 주식 거래 요청")
public record CurrentPriceTradingRequest(
    @Schema(description = "종목코드", example = "005930", required = true)
    @NotBlank(message = "종목코드는 필수입니다.")
    String stockId,

    @Schema(description = "시장코드", example = "J", required = true)
    @NotBlank(message = "시장코드는 필수입니다.")
    String marketCode,

    @Schema(description = "거래 수량", example = "10", required = true)
    @NotNull(message = "거래 수량은 필수입니다.")
    @Min(value = 1, message = "거래 수량은 1 이상이어야 합니다.")
    Integer quantity
) {}
