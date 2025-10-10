package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Schema(description = "주식현재가 응답")
@Builder
public record StockCurrentPriceResponse(
    @Schema(description = "주식 현재가", example = "70000")
    BigDecimal stckPrpr,
    
    @Schema(description = "전일대비", example = "1500")
    BigDecimal prdyVrss,
    
    @Schema(description = "전일 대비율", example = "2.19")
    BigDecimal prdyCtrt,
    
    @Schema(description = "주식 최고가", example = "71000")
    BigDecimal stckHgpr,
    
    @Schema(description = "주식 최저가", example = "69000")
    BigDecimal stckLwpr
) {}
