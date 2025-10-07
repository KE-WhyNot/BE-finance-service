package com.youthfi.finance.domain.portfolio.application.dto.response;

import java.math.BigDecimal;

public record StockPriceInfoResponse(
    String stockId,
    BigDecimal currentPrice,
    BigDecimal w52HighPrice,
    BigDecimal w52LowPrice
) {
    public static StockPriceInfoResponse getDefault(String stockId) {
        return new StockPriceInfoResponse(
            stockId,
            BigDecimal.valueOf(100000),
            BigDecimal.valueOf(100000),
            BigDecimal.valueOf(100000)
        );
    }
}