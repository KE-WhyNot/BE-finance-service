package com.youthfi.finance.domain.stock.domain.service;

import com.youthfi.finance.domain.stock.application.dto.request.ChartRequest;
import com.youthfi.finance.domain.stock.application.dto.response.ChartDataResponse;
import com.youthfi.finance.domain.stock.infra.KisChartApiClient;
import com.youthfi.finance.global.exception.StockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 차트 데이터 서비스 (캐시 + KIS API)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChartService {

    private final ChartCacheService chartCacheService;
    private final KisChartApiClient kisChartApiClient;
    
    /**
     * 차트 데이터 조회 (캐시 우선)
     */
    public ChartDataResponse getChartData(ChartRequest request) {
        if (!request.isValidPeriod() || !request.isValidRange()) {
            log.warn("유효하지 않은 요청: {}", request);
            return ChartDataResponse.empty(request.stockCode(), request.period(), request.range());
        }
        
        // 1. 캐시에서 조회
        ChartDataResponse cachedData = getCachedData(request);
        if ("1min".equals(request.period()) && cachedData != null && cachedData.candles() != null && !cachedData.candles().isEmpty()) {
            // 분봉: 스냅샷 + 증분 병합
            String lastHHmm = cachedData.candles().get(cachedData.candles().size() - 1).time().replace(":", "");
            ChartDataResponse incremental = kisChartApiClient.getMinuteChartSince(request.stockCode(), lastHHmm);
            if (incremental != null && incremental.candles() != null && !incremental.candles().isEmpty()) {
                java.util.List<com.youthfi.finance.domain.stock.application.dto.response.CandleDataResponse> merged = new java.util.ArrayList<>(cachedData.candles());
                merged.addAll(incremental.candles());
                ChartDataResponse mergedResp = new ChartDataResponse(
                        request.stockCode(),
                        request.period(),
                        request.range(),
                        merged,
                        incremental.lastUpdated()
                );
                saveToCache(request, mergedResp);
                return mergedResp;
            }
            return cachedData;
        }
        if (cachedData != null && !cachedData.candles().isEmpty()) {
            log.debug("캐시에서 데이터 반환: {}", request);
            return cachedData;
        }

        // 2. KIS API에서 조회 (스냅샷 생성)
        ChartDataResponse apiData = getApiData(request);

        // 3. 캐시에 저장
        saveToCache(request, apiData);

        return apiData;
    }
    
    /**
     * 일봉 데이터 조회 (60일)
     */
    public ChartDataResponse getDailyChart(String stockCode) {
        ChartRequest request = new ChartRequest(stockCode, "1d", "60d");
        return getChartData(request);
    }
    
    /**
     * 월봉 데이터 조회 (5년)
     */
    public ChartDataResponse getMonthlyChart(String stockCode) {
        ChartRequest request = new ChartRequest(stockCode, "1m", "5y");
        return getChartData(request);
    }
    
    /**
     * 연봉 데이터 조회 (10년)
     */
    public ChartDataResponse getYearlyChart(String stockCode) {
        ChartRequest request = new ChartRequest(stockCode, "1y", "10y");
        return getChartData(request);
    }
    
    /**
     * 캐시에서 데이터 조회
     */
    private ChartDataResponse getCachedData(ChartRequest request) {
        return switch (request.period()) {
            case "1d" -> chartCacheService.getDailyChart(request.stockCode(), request.range());
            case "1m" -> chartCacheService.getMonthlyChart(request.stockCode(), request.range());
            case "1y" -> chartCacheService.getYearlyChart(request.stockCode(), request.range());
            case "1min" -> chartCacheService.getMinuteChart(request.stockCode());
            default -> null;
        };
    }
    
    /**
     * KIS API에서 데이터 조회
     */
    private ChartDataResponse getApiData(ChartRequest request) {
        return switch (request.period()) {
            case "1d" -> {
                int days = extractDays(request.range());
                yield kisChartApiClient.getDailyChart(request.stockCode(), days);
            }
            case "1m" -> {
                int years = extractYears(request.range());
                yield kisChartApiClient.getMonthlyChart(request.stockCode(), years);
            }
            case "1y" -> {
                int years = extractYears(request.range());
                yield kisChartApiClient.getYearlyChart(request.stockCode(), years);
            }
            case "1min" -> kisChartApiClient.getMinuteChart(request.stockCode());
            
            default -> ChartDataResponse.empty(request.stockCode(), request.period(), request.range());
        };
    }
    
    /**
     * 캐시에 데이터 저장
     */
    private void saveToCache(ChartRequest request, ChartDataResponse data) {
        if (data == null || data.candles().isEmpty()) {
            return;
        }
        
        switch (request.period()) {
            case "1d" -> chartCacheService.saveDailyChart(request.stockCode(), request.range(), data);
            case "1m" -> chartCacheService.saveMonthlyChart(request.stockCode(), request.range(), data);
            case "1y" -> chartCacheService.saveYearlyChart(request.stockCode(), request.range(), data);
            case "1min" -> chartCacheService.saveMinuteChart(request.stockCode(), data);
        }
    }

    /**
     * 분봉 데이터 조회 (today)
     */
    public ChartDataResponse getMinuteChart(String stockCode) {
        ChartRequest request = new ChartRequest(stockCode, "1min", "today");
        return getChartData(request);
    }
    
    /**
     * 범위에서 일수 추출
     */
    private int extractDays(String range) {
        if (range.endsWith("d")) {
            return Integer.parseInt(range.substring(0, range.length() - 1));
        }
        return 60; // 기본값
    }
    
    /**
     * 범위에서 연수 추출
     */
    private int extractYears(String range) {
        if (range.endsWith("y")) {
            return Integer.parseInt(range.substring(0, range.length() - 1));
        }
        return 5; // 기본값
    }
}
