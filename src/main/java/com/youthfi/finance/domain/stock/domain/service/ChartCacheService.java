package com.youthfi.finance.domain.stock.domain.service;

import com.youthfi.finance.domain.stock.application.dto.response.ChartDataResponse;
import com.youthfi.finance.global.config.RedisConstants;
import com.youthfi.finance.global.exception.StockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 차트 데이터 Redis 캐싱 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChartCacheService {

    private final RedisTemplate<String, ChartDataResponse> redisChartDataTemplate;
    
    /**
     * 일봉 데이터 캐시 저장
     */
    public void saveDailyChart(String stockCode, String range, ChartDataResponse chartData) {
        String key = RedisConstants.buildDailyChartKey(stockCode, range);
        redisChartDataTemplate.opsForValue().set(key, chartData, Duration.ofHours(1));
        log.debug("일봉 캐시 저장: key={}", key);
    }
    
    /**
     * 월봉 데이터 캐시 저장
     */
    public void saveMonthlyChart(String stockCode, String range, ChartDataResponse chartData) {
        String key = RedisConstants.buildMonthlyChartKey(stockCode, range);
        redisChartDataTemplate.opsForValue().set(key, chartData, Duration.ofDays(1));
        log.debug("월봉 캐시 저장: key={}", key);
    }
    
    /**
     * 연봉 데이터 캐시 저장
     */
    public void saveYearlyChart(String stockCode, String range, ChartDataResponse chartData) {
        String key = RedisConstants.buildYearlyChartKey(stockCode, range);
        redisChartDataTemplate.opsForValue().set(key, chartData, Duration.ofDays(7));
        log.debug("연봉 캐시 저장: key={}", key);
    }
    
    // 분봉 캐시 메서드 제거됨 - 실시간 전용
    
    /**
     * 일봉 데이터 캐시 조회
     */
    public ChartDataResponse getDailyChart(String stockCode, String range) {
        String key = RedisConstants.buildDailyChartKey(stockCode, range);
        ChartDataResponse data = redisChartDataTemplate.opsForValue().get(key);
        log.debug("일봉 캐시 조회: key={}, found={}", key, data != null);
        return data;
    }
    
    /**
     * 월봉 데이터 캐시 조회
     */
    public ChartDataResponse getMonthlyChart(String stockCode, String range) {
        String key = RedisConstants.buildMonthlyChartKey(stockCode, range);
        ChartDataResponse data = redisChartDataTemplate.opsForValue().get(key);
        log.debug("월봉 캐시 조회: key={}, found={}", key, data != null);
        return data;
    }
    
    /**
     * 연봉 데이터 캐시 조회
     */
    public ChartDataResponse getYearlyChart(String stockCode, String range) {
        String key = RedisConstants.buildYearlyChartKey(stockCode, range);
        ChartDataResponse data = redisChartDataTemplate.opsForValue().get(key);
        log.debug("연봉 캐시 조회: key={}, found={}", key, data != null);
        return data;
    }
    
    // 분봉 캐시 메서드 제거됨 - 실시간 전용
    
    /**
     * 캐시 삭제
     */
    public void deleteChart(String stockCode, String period, String range) {
        String key = getChartKeyByPeriod(period, stockCode, range);
        redisChartDataTemplate.delete(key);
        log.debug("캐시 삭제: key={}", key);
    }
    
    /**
     * 기간별 차트 키 생성
     */
    private String getChartKeyByPeriod(String period, String stockCode, String range) {
        return switch (period) {
            case "1d" -> RedisConstants.buildDailyChartKey(stockCode, range);
            case "1m" -> RedisConstants.buildMonthlyChartKey(stockCode, range);
            case "1y" -> RedisConstants.buildYearlyChartKey(stockCode, range);
            // 분봉은 캐시 없이 실시간만
            default -> throw new IllegalArgumentException("Invalid chart period: " + period);
        };
    }

}
