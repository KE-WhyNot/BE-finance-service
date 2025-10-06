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
public class PortfolioRiskAnalysis {
    private BigDecimal originalInvestment;  // 원래 투자금액 (1000만원)
    private BigDecimal highestValue;        // 52주 최고가 기준 자산
    private BigDecimal lowestValue;         // 52주 최저가 기준 자산
    private BigDecimal currentValue;        // 현재가 기준 자산
    private BigDecimal highestReturn;       // 최고 수익률 (%)
    private BigDecimal lowestReturn;        // 최저 수익률 (%)
    private BigDecimal currentReturn;       // 현재 수익률 (%)
    private String riskLevel;               // HIGH/MEDIUM/LOW
    
    public static PortfolioRiskAnalysis getDefault(BigDecimal investment) {
        return PortfolioRiskAnalysis.builder()
                .originalInvestment(investment)
                .highestValue(investment)
                .lowestValue(investment)
                .currentValue(investment)
                .highestReturn(BigDecimal.ZERO)
                .lowestReturn(BigDecimal.ZERO)
                .currentReturn(BigDecimal.ZERO)
                .riskLevel("MEDIUM")
                .build();
    }
}