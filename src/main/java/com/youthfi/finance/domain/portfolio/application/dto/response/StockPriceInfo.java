package com.youthfi.finance.domain.portfolio.application.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockPriceInfo {
    private String stockId;
    private BigDecimal currentPrice;    // stck_prpr
    private BigDecimal w52HighPrice;    // w52_hgpr  
    private BigDecimal w52LowPrice;     // w52_lwpr
    
    public static StockPriceInfo getDefault(String stockId) {
        return StockPriceInfo.builder()
                .stockId(stockId)
                .currentPrice(BigDecimal.valueOf(100000))
                .w52HighPrice(BigDecimal.valueOf(100000))
                .w52LowPrice(BigDecimal.valueOf(100000))
                .build();
    }
}