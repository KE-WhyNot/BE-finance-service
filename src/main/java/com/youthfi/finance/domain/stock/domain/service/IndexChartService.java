package com.youthfi.finance.domain.stock.domain.service;

import com.youthfi.finance.domain.stock.application.dto.request.IndexChartRequest;
import com.youthfi.finance.domain.stock.application.dto.response.IndexChartDataResponse;
import com.youthfi.finance.domain.stock.infra.KisIndexChartApiClient;
import com.youthfi.finance.global.exception.StockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 지수 차트 데이터 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IndexChartService {

    private final KisIndexChartApiClient kisIndexChartApiClient;
    
    /**
     * 지수 차트 데이터 조회
     */
    public IndexChartDataResponse getIndexChartData(IndexChartRequest request) {
        if (!request.isValid()) {
            log.warn("유효하지 않은 지수 차트 요청: {}", request);
            return IndexChartDataResponse.empty(request.indexCode(), request.period(), request.range());
        }
        
        return switch (request.period()) {
            case "D" -> getDailyIndexChart(request.indexCode());
            case "W" -> getWeeklyIndexChart(request.indexCode());
            case "M" -> getMonthlyIndexChart(request.indexCode());
            default -> IndexChartDataResponse.empty(request.indexCode(), request.period(), request.range());
        };
    }
    
    /**
     * 일봉 데이터 조회 (30일)
     */
    public IndexChartDataResponse getDailyIndexChart(String indexCode) {
        log.info("지수 일봉 데이터 조회 요청 - 지수코드: {}", indexCode);
        
        IndexChartDataResponse response = kisIndexChartApiClient.getDailyIndexChart(indexCode);
        
        log.info("지수 일봉 데이터 조회 완료 - 지수코드: {}, 캔들 수: {}", 
                indexCode, response.candles().size());
        return response;
    }
    
    /**
     * 주봉 데이터 조회 (30주)
     */
    public IndexChartDataResponse getWeeklyIndexChart(String indexCode) {
        log.info("지수 주봉 데이터 조회 요청 - 지수코드: {}", indexCode);
        
        IndexChartDataResponse response = kisIndexChartApiClient.getWeeklyIndexChart(indexCode);
        
        log.info("지수 주봉 데이터 조회 완료 - 지수코드: {}, 캔들 수: {}", 
                indexCode, response.candles().size());
        return response;
    }
    
    /**
     * 월봉 데이터 조회 (30개월)
     */
    public IndexChartDataResponse getMonthlyIndexChart(String indexCode) {
        log.info("지수 월봉 데이터 조회 요청 - 지수코드: {}", indexCode);
        
        IndexChartDataResponse response = kisIndexChartApiClient.getMonthlyIndexChart(indexCode);
        
        log.info("지수 월봉 데이터 조회 완료 - 지수코드: {}, 캔들 수: {}", 
                indexCode, response.candles().size());
        return response;
    }
    
    /**
     * 지수 코드 유효성 검증
     */
    public boolean isValidIndexCode(String indexCode) {
        return "0001".equals(indexCode) || "1001".equals(indexCode);
    }
    
    /**
     * 지수 코드를 한글명으로 변환
     */
    public String getIndexName(String indexCode) {
        return switch (indexCode) {
            case "0001" -> "코스피";
            case "1001" -> "코스닥";
            default -> "알 수 없는 지수";
        };
    }
}
