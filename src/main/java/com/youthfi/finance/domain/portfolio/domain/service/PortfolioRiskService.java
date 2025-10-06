package com.youthfi.finance.domain.portfolio.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioRiskAnalysis;
import com.youthfi.finance.domain.portfolio.application.dto.response.StockPriceInfo;
import com.youthfi.finance.domain.stock.infra.StockCurrentPriceApiClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioRiskService {

    private final StockCurrentPriceApiClient stockCurrentPriceApiClient;

    public PortfolioRiskAnalysis calculatePortfolioRisk(List<PortfolioResponse.RecommendedStock> recommendedStocks, BigDecimal investmentAmount){
        try {
            log.info("포트폴리오 리스크 분석 시작: {}개 종목, 투자금액: {}",
                recommendedStocks.size(), investmentAmount);

            BigDecimal totalHighValue = BigDecimal.ZERO;
            BigDecimal totalLowValue = BigDecimal.ZERO;
            BigDecimal currentValue = BigDecimal.ZERO;

            for (PortfolioResponse.RecommendedStock stock : recommendedStocks) {
                StockPriceInfo priceInfo = getStockPriceInfo(stock.getStockId());

                BigDecimal stockinvestment = investmentAmount // 종목당 투자 금액
                        .multiply(stock.getAllocationPct())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                BigDecimal highValue = stockinvestment
                        .multiply(priceInfo.getW52HighPrice())
                        .divide(priceInfo.getCurrentPrice(), 2, RoundingMode.HALF_UP);

                BigDecimal lowValue = stockinvestment
                        .multiply(priceInfo.getW52LowPrice())
                        .divide(priceInfo.getCurrentPrice(), 2, RoundingMode.HALF_UP);

                BigDecimal currentStockValue = stockinvestment; 

                totalHighValue = totalHighValue.add(highValue);
                totalLowValue = totalLowValue.add(lowValue);
                currentValue = currentValue.add(currentStockValue);
            }

            BigDecimal highReturn = calculateReturnRate(investmentAmount, totalHighValue);
            BigDecimal lowReturn = calculateReturnRate(investmentAmount, totalLowValue);
            BigDecimal currentReturn = calculateReturnRate(investmentAmount, currentValue);

            return PortfolioRiskAnalysis.builder()
                    .originalInvestment(investmentAmount)
                    .highestValue(totalHighValue)
                    .lowestValue(totalLowValue)
                    .currentValue(currentValue)
                    .highestReturn(highReturn)
                    .lowestReturn(lowReturn)
                    .currentReturn(currentReturn)
                    .riskLevel(calculateRiskLevel(highReturn, lowReturn))
                    .build();

        } catch (Exception e) {
            log.error("포트폴리오 리스크 분석 실패", e);
            return PortfolioRiskAnalysis.getDefault(investmentAmount);
        }
    }


    private StockPriceInfo getStockPriceInfo(String stockId) {
        try {
            Map<String, Object> response = stockCurrentPriceApiClient.getStockCurrentPrice("J", stockId);

            return StockPriceInfo.builder()
                    .stockId(stockId)
                    .currentPrice(new BigDecimal(response.get("stck_prpr").toString()))
                    .w52HighPrice(new BigDecimal(response.get("w52_hgpr").toString()))
                    .w52LowPrice(new BigDecimal(response.get("w52_lwpr").toString()))
                    .build();

        } catch (Exception e) {
            log.error("주식 가격 정보 조회 실패", e);
            return StockPriceInfo.getDefault(stockId);
        }
    }

    private BigDecimal calculateReturnRate(BigDecimal original, BigDecimal current) {
        return current.subtract(original)
                .divide(original, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private String calculateRiskLevel(BigDecimal highReturn, BigDecimal lowReturn) {
        BigDecimal riskRange = highReturn.subtract(lowReturn);

        if(riskRange.compareTo(BigDecimal.valueOf(50)) > 0) {
            return "HIGH";
        } else if (riskRange.compareTo(BigDecimal.valueOf(20)) > 0) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
}


