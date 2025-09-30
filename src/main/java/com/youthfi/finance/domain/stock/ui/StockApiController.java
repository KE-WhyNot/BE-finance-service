package com.youthfi.finance.domain.stock.ui;

import com.youthfi.finance.domain.stock.infra.StockCurrentPriceApiClient;
import com.youthfi.finance.domain.stock.infra.DividendScheduleApiClient;
import com.youthfi.finance.global.service.KisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockApiController {
    
    private final StockCurrentPriceApiClient stockCurrentPriceApiClient;
    private final DividendScheduleApiClient dividendScheduleApiClient;
    private final KisTokenService kisTokenService;
    
    /**
     * 주식현재가 시세 조회
     * @param marketCode 시장구분코드 (J:KRX, N:NXT, U:N:통합)
     * @param stockCode 종목코드 (ex: 005930)
     * @return 주식현재가 시세 데이터
     */
    @GetMapping("/current-price")
    public Map<String, Object> getStockCurrentPrice(
            @RequestParam String marketCode,
            @RequestParam String stockCode) {
        return stockCurrentPriceApiClient.getStockCurrentPrice(marketCode, stockCode);
    }
    
    /**
     * 예탁원정보(배당일정) 조회
     * @param stockCode 종목코드 (ex: 005930)
     * @return 배당일정 데이터
     */
    @GetMapping("/dividend-schedule")
    public Map<String, Object> getDividendSchedule(@RequestParam String stockCode) {
        return dividendScheduleApiClient.getDividendSchedule(stockCode);
    }
    
    /**
     * KIS API 토큰 상태 조회
     * @return 토큰 상태 정보
     */
    @GetMapping("/token-status")
    public Map<String, Map<String, Object>> getTokenStatus() {
        return kisTokenService.getAllTokenStatus();
    }
}
