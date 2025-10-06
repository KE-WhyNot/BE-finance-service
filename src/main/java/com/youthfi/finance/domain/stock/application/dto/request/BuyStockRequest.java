package com.youthfi.finance.domain.stock.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "주식 매수 요청")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyStockRequest {

    @Schema(description = "종목코드", example = "005930", required = true)
    @NotBlank(message = "종목코드는 필수입니다.")
    private String stockId;

    @Schema(description = "매수 수량", example = "10", required = true)
    @NotNull(message = "매수 수량은 필수입니다.")
    @Min(value = 1, message = "매수 수량은 1 이상이어야 합니다.")
    private Integer quantity;

    @Schema(description = "매수 가격", example = "70000.00", required = true)
    @NotNull(message = "매수 가격은 필수입니다.")
    @DecimalMin(value = "0.01", message = "매수 가격은 0.01 이상이어야 합니다.")
    private BigDecimal price;
}
