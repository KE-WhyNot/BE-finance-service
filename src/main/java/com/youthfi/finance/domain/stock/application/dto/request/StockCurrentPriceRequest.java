package com.youthfi.finance.domain.stock.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "주식현재가 조회 요청")
public record StockCurrentPriceRequest(
    @Schema(description = "시장코드", example = "J", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "시장코드는 필수입니다.")
    String marketCode,

    @Schema(description = "종목코드", example = "005930", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "종목코드는 필수입니다.")
    String stockCode
) {}
