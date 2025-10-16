package com.youthfi.finance.domain.portfolio.application.dto.response;

import java.math.BigDecimal;

public record PortfolioRiskAnalysisResponse(
    BigDecimal originalInvestment,
    BigDecimal highestValue,
    BigDecimal lowestValue,
    BigDecimal currentValue,
    BigDecimal highestReturn,
    BigDecimal lowestReturn,
    BigDecimal currentReturn
) {
    public static PortfolioRiskAnalysisResponse getDefault(BigDecimal investment) {
        return new PortfolioRiskAnalysisResponse(
            investment,
            investment,
            investment,
            investment,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO
        );
    }
}