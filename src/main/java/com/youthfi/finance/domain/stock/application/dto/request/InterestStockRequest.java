package com.youthfi.finance.domain.stock.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "관심종목 요청")
public record InterestStockRequest(
    @Schema(description = "종목코드", example = "005930", required = true)
    @NotBlank(message = "종목코드는 필수입니다.")
    String stockId
) {}
