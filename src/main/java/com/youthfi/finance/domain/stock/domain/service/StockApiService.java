package com.youthfi.finance.domain.stock.domain.service;

import com.youthfi.finance.domain.stock.infra.DividendScheduleApiClient;
import com.youthfi.finance.domain.stock.infra.StockCurrentPriceApiClient;
import com.youthfi.finance.global.service.KisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockApiService {

    private final StockCurrentPriceApiClient stockCurrentPriceApiClient;
    private final DividendScheduleApiClient dividendScheduleApiClient;
    private final KisTokenService kisTokenService;

    /**
     * 주식현재가 시세 조회 
     */
    public Map<String, Object> getStockCurrentPrice(String marketCode, String stockCode) {
        return stockCurrentPriceApiClient.getStockCurrentPrice(marketCode, stockCode);
    }

    /**
     * 배당일정 조회 
     */
    public Map<String, Object> getDividendSchedule(String stockCode) {
        return dividendScheduleApiClient.getDividendSchedule(stockCode);
    }

    /**
     * KIS API 토큰 상태 조회
     */
    public Map<String, Map<String, Object>> getTokenStatus() {
        return kisTokenService.getAllTokenStatus();
    }
}
