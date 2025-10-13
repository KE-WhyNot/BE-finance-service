package com.youthfi.finance.domain.portfolio.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioRiskAnalysisResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.StockPriceInfoResponse;
import com.youthfi.finance.domain.stock.infra.StockCurrentPriceApiClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioRiskService {

    private final StockCurrentPriceApiClient stockCurrentPriceApiClient;

    public PortfolioRiskAnalysisResponse calculatePortfolioRisk(List<PortfolioResponse.RecommendedStock> recommendedStocks, BigDecimal investmentAmount){
        try {
            log.info("포트폴리오 리스크 분석 시작: {}개 종목, 투자금액: {}",
                recommendedStocks.size(), investmentAmount);

            BigDecimal totalHighValue = BigDecimal.ZERO;
            BigDecimal totalLowValue = BigDecimal.ZERO;
            BigDecimal currentValue = BigDecimal.ZERO;

            for (PortfolioResponse.RecommendedStock stock : recommendedStocks) {
                StockPriceInfoResponse priceInfo = getStockPriceInfo(stock.stockId());

                BigDecimal stockinvestment = investmentAmount // 종목당 투자 금액
                        .multiply(stock.allocationPct())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                BigDecimal highValue = stockinvestment
                        .multiply(priceInfo.w52HighPrice())
                        .divide(priceInfo.currentPrice(), 2, RoundingMode.HALF_UP);

                BigDecimal lowValue = stockinvestment
                        .multiply(priceInfo.w52LowPrice())
                        .divide(priceInfo.currentPrice(), 2, RoundingMode.HALF_UP);

                BigDecimal currentStockValue = stockinvestment; 

                totalHighValue = totalHighValue.add(highValue);
                totalLowValue = totalLowValue.add(lowValue);
                currentValue = currentValue.add(currentStockValue);
            }

            BigDecimal highReturn = calculateReturnRate(investmentAmount, totalHighValue);
            BigDecimal lowReturn = calculateReturnRate(investmentAmount, totalLowValue);
            BigDecimal currentReturn = calculateReturnRate(investmentAmount, currentValue);

            return new PortfolioRiskAnalysisResponse(
                    investmentAmount,
                    totalHighValue,
                    totalLowValue,
                    currentValue,
                    highReturn,
                    lowReturn,
                    currentReturn,
                    calculateRiskLevel(highReturn, lowReturn)
            );

        } catch (Exception e) {
            log.error("포트폴리오 리스크 분석 실패", e);
            return PortfolioRiskAnalysisResponse.getDefault(investmentAmount);
        }
    }


    private StockPriceInfoResponse getStockPriceInfo(String stockId) {
        try {
            Map<String, Object> response = stockCurrentPriceApiClient.getStockCurrentPrice("J", stockId);

            return new StockPriceInfoResponse(
                    stockId,
                    new BigDecimal(response.get("stck_prpr").toString()),
                    new BigDecimal(response.get("w52_hgpr").toString()),
                    new BigDecimal(response.get("w52_lwpr").toString())
            );

        } catch (Exception e) {
            log.error("주식 가격 정보 조회 실패", e);
            return StockPriceInfoResponse.getDefault(stockId);
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


