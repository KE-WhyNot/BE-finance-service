package com.youthfi.finance.domain.portfolio.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.youthfi.finance.domain.portfolio.application.dto.response.CompleteInvestmentProfileResponse;
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

    public PortfolioRiskAnalysisResponse calculatePortfolioRisk(List<CompleteInvestmentProfileResponse.RecommendedStock> recommendedStocks, BigDecimal investmentAmount){
        try {
            log.info("포트폴리오 리스크 분석 시작: {}개 종목, 투자금액: {}",
                recommendedStocks.size(), investmentAmount);

            BigDecimal totalHighValue = BigDecimal.ZERO;
            BigDecimal totalLowValue = BigDecimal.ZERO;
            BigDecimal currentValue = BigDecimal.ZERO;

            for (CompleteInvestmentProfileResponse.RecommendedStock stock : recommendedStocks) {
                StockPriceInfoResponse priceInfo = getStockPriceInfo(stock.stockId());

                if (priceInfo.currentPrice() == null || priceInfo.currentPrice().compareTo(BigDecimal.ZERO) == 0) {
                    log.warn("종목 현재가가 null 또는 0입니다. 포트폴리오 계산에서 제외: stockId={}, currentPrice={}",
                            stock.stockId(), priceInfo.currentPrice());
                    continue;
                }

                BigDecimal stockinvestment = investmentAmount // 종목당 투자 금액
                        .multiply(stock.allocationPct())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                // 52주 고가/저가 (getStockPriceInfo에서 이미 안전 보정됨)
                BigDecimal w52HighPrice = priceInfo.w52HighPrice();
                BigDecimal w52LowPrice = priceInfo.w52LowPrice();
                BigDecimal currentPrice = priceInfo.currentPrice();

                // 52주 고가가 현재가보다 낮거나 같으면 리스크 없음
                if (w52HighPrice.compareTo(currentPrice) <= 0) {
                    w52HighPrice = currentPrice;
                }
                
                // 52주 저가가 현재가보다 높거나 같으면 리스크 없음  
                if (w52LowPrice.compareTo(currentPrice) >= 0) {
                    w52LowPrice = currentPrice;
                }

                BigDecimal highValue = stockinvestment
                        .multiply(w52HighPrice)
                        .divide(currentPrice, 2, RoundingMode.HALF_UP);

                BigDecimal lowValue = stockinvestment
                        .multiply(w52LowPrice)
                        .divide(currentPrice, 2, RoundingMode.HALF_UP);

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
                    currentReturn
            );

        } catch (Exception e) {
            log.error("포트폴리오 리스크 분석 실패", e);
            return PortfolioRiskAnalysisResponse.getDefault(investmentAmount);
        }
    }


    private StockPriceInfoResponse getStockPriceInfo(String stockId) {
        try {
            Map<String, Object> response = stockCurrentPriceApiClient.getStockCurrentPrice("J", stockId);
            if (response == null) {
                log.warn("KIS 현재가 응답이 null 입니다: stockId={}", stockId);
                return StockPriceInfoResponse.getDefault(stockId);
            }

            Object outputObj = response.get("output");
            @SuppressWarnings("unchecked")
            Map<String, Object> payload = (outputObj instanceof Map) ? (Map<String, Object>) outputObj : response;

            Object prprObj = payload.get("stck_prpr");
            if (prprObj == null) {
                log.warn("현재가(stck_prpr) 누락: stockId={}, rawKeys={}", stockId, payload.keySet());
                // 서비스에서 제외되도록 currentPrice를 null로 반환
                return new StockPriceInfoResponse(
                        stockId,
                        null,
                        null,
                        null
                );
            }

            BigDecimal currentPrice = toBigDecimalSafe(prprObj, BigDecimal.ZERO);

            // 52주 고저가가 없으면 currentPrice로 대체하여 리스크 기여 0 처리
            Object w52HighObj = payload.get("w52_hgpr");
            Object w52LowObj = payload.get("w52_lwpr");

            BigDecimal w52HighPrice = toBigDecimalSafe(
                    (w52HighObj != null ? w52HighObj : currentPrice), currentPrice);
            BigDecimal w52LowPrice = toBigDecimalSafe(
                    (w52LowObj != null ? w52LowObj : currentPrice), currentPrice);

            return new StockPriceInfoResponse(
                    stockId,
                    currentPrice,
                    w52HighPrice,
                    w52LowPrice
            );

        } catch (Exception e) {
            log.error("주식 가격 정보 조회 실패", e);
            return StockPriceInfoResponse.getDefault(stockId);
        }
    }

    private BigDecimal toBigDecimalSafe(Object value, BigDecimal defaultVal) {
        try {
            if (value instanceof BigDecimal bd) return bd;
            return new BigDecimal(value.toString());
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    private BigDecimal calculateReturnRate(BigDecimal original, BigDecimal current) {
        if (original.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("원본 투자금액이 0 이하입니다. 수익률 계산에서 0 반환: original={}, current={}", original, current);
            return BigDecimal.ZERO;
        }
        return current.subtract(original)
                .divide(original, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }


}


