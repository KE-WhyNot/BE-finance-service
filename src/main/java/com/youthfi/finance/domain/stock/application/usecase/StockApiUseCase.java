package com.youthfi.finance.domain.stock.application.usecase;

import com.youthfi.finance.domain.stock.application.dto.request.DividendScheduleRequest;
import com.youthfi.finance.domain.stock.application.dto.request.StockCurrentPriceRequest;
import com.youthfi.finance.domain.stock.domain.service.StockApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StockApiUseCase {

    private final StockApiService stockApiService;

    /**
     * 주식현재가 시세 조회 
     */
    public Map<String, Object> getStockCurrentPrice(StockCurrentPriceRequest request) {
        log.info("주식현재가 시세 조회 요청 - 시장코드: {}, 종목코드: {}", request.marketCode(), request.stockCode());
        
        Map<String, Object> result = stockApiService.getStockCurrentPrice(request.marketCode(), request.stockCode());
        
        log.info("주식현재가 시세 조회 완료 - 시장코드: {}, 종목코드: {}", request.marketCode(), request.stockCode());
        return result;
    }

    /**
     * 배당일정 조회
     */
    public Map<String, Object> getDividendSchedule(DividendScheduleRequest request) {
        log.info("배당일정 조회 요청 - 종목코드: {}", request.stockCode());
        
        Map<String, Object> result = stockApiService.getDividendSchedule(request.stockCode());
        
        log.info("배당일정 조회 완료 - 종목코드: {}", request.stockCode());
        return result;
    }

    /**
     * KIS API 토큰 상태 조회 
     */
    public Map<String, Map<String, Object>> getTokenStatus() {
        log.info("KIS API 토큰 상태 조회 요청");
        
        Map<String, Map<String, Object>> result = stockApiService.getTokenStatus();
        
        log.info("KIS API 토큰 상태 조회 완료");
        return result;
    }
}
