package com.youthfi.finance.domain.stock.application.usecase;

import com.youthfi.finance.domain.stock.application.dto.request.DividendScheduleRequest;
import com.youthfi.finance.domain.stock.application.dto.request.StockCurrentPriceRequest;
import com.youthfi.finance.domain.stock.application.dto.response.StockCurrentPriceResponse;
import com.youthfi.finance.domain.stock.domain.service.StockApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public StockCurrentPriceResponse getStockCurrentPrice(StockCurrentPriceRequest request) {
        log.info("주식현재가 시세 조회 요청 - 시장코드: {}, 종목코드: {}", request.marketCode(), request.stockCode());
        
        Map<String, Object> result = stockApiService.getStockCurrentPrice(request.marketCode(), request.stockCode());
        
        // KIS API 응답에서 output 필드 추출
        Map<String, Object> output = (Map<String, Object>) result.get("output");
        if (output == null) {
            log.error("KIS API 응답에서 output 필드를 찾을 수 없습니다. 응답: {}", result);
            throw new RuntimeException("KIS API 응답 구조가 올바르지 않습니다.");
        }
        
        // 응답 데이터 파싱
        StockCurrentPriceResponse response = StockCurrentPriceResponse.builder()
                .stckPrpr(parseBigDecimal(output.get("stck_prpr")))
                .prdyVrss(parseBigDecimal(output.get("prdy_vrss")))
                .prdyCtrt(parseBigDecimal(output.get("prdy_ctrt")))
                .stckHgpr(parseBigDecimal(output.get("stck_hgpr")))
                .stckLwpr(parseBigDecimal(output.get("stck_lwpr")))
                .build();
        
        log.info("주식현재가 시세 조회 완료 - 시장코드: {}, 종목코드: {}, 현재가: {}", 
                request.marketCode(), request.stockCode(), response.stckPrpr());
        return response;
    }
    
    /**
     * Object를 BigDecimal로 변환하는 헬퍼 메서드
     */
    private BigDecimal parseBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            log.warn("BigDecimal 변환 실패: {}", value);
            return null;
        }
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
