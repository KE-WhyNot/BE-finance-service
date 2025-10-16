package com.youthfi.finance.global.config;

/**
 * Redis 키 패턴 상수 클래스
 */
public class RedisConstants {
    
    // KIS API 관련 Redis 키 패턴
    public static final String KIS_TOKEN_PREFIX = "kis:token:";
    public static final String KIS_EXPIRY_PREFIX = "kis:expiry:";
    public static final String KIS_WS_APPROVAL_PREFIX = "kis:ws:approval:";
    public static final String KIS_WS_EXPIRY_PREFIX = "kis:ws:expiry:";
    
    // 차트 캐시 관련 Redis 키 패턴
    public static final String CHART_CACHE_PREFIX = "chart:";
    public static final String CHART_DAILY_PREFIX = "daily:";
    public static final String CHART_MONTHLY_PREFIX = "monthly:";
    public static final String CHART_YEARLY_PREFIX = "yearly:";
    public static final String CHART_MINUTE_PREFIX = "minute:";
    
    // Redis 키 생성 메서드
    public static String buildKisTokenKey(String appkey) {
        return KIS_TOKEN_PREFIX + appkey;
    }
    
    public static String buildKisExpiryKey(String appkey) {
        return KIS_EXPIRY_PREFIX + appkey;
    }
    public static String buildKisWsApprovalKey(String appkey) {
        return KIS_WS_APPROVAL_PREFIX + appkey;
    }
    public static String buildKisWsExpiryKey(String appkey) {
        return KIS_WS_EXPIRY_PREFIX + appkey;
    }
    
    public static String buildChartCacheKey(String chartType, String stockCode, String range) {
        return CHART_CACHE_PREFIX + chartType + stockCode + ":" + range;
    }
    
    public static String buildDailyChartKey(String stockCode, String range) {
        return buildChartCacheKey(CHART_DAILY_PREFIX, stockCode, range);
    }
    
    public static String buildMonthlyChartKey(String stockCode, String range) {
        return buildChartCacheKey(CHART_MONTHLY_PREFIX, stockCode, range);
    }
    
    public static String buildYearlyChartKey(String stockCode, String range) {
        return buildChartCacheKey(CHART_YEARLY_PREFIX, stockCode, range);
    }
    
    public static String buildMinuteChartKey(String stockCode, String range) {
        return buildChartCacheKey(CHART_MINUTE_PREFIX, stockCode, range);
    }
}
