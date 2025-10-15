package com.youthfi.finance.global.config.properties;

/**
 * KIS API 엔드포인트 상수 클래스
 */
public class KisApiEndpoints {
    
    // Base URLs
    public static final String REAL_BASE_URL = "https://openapi.koreainvestment.com:9443";
    
    // 국내주식 API 엔드포인트
    public static final String DOMESTIC_STOCK_BASE = "/uapi/domestic-stock/v1";
    
    // 주식현재가 시세 API
    public static final String STOCK_CURRENT_PRICE = DOMESTIC_STOCK_BASE + "/quotations/inquire-price";
    public static final String STOCK_CURRENT_PRICE_TR_ID = "FHKST01010100";
    
    // API 카테고리
    public static final String DOMESTIC_STOCK = "국내주식";
    
    // WebSocket 승인 키 발급 API
    public static final String WEBSOCKET_APPROVAL = "/oauth2/Approval";
    
    // 토큰 발급 API
    public static final String TOKEN_ISSUANCE = "/oauth2/tokenP";
    
    // 차트 API 엔드포인트
    public static final String DAILY_CHART = "/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice";
    public static final String MINUTE_CHART = "/uapi/domestic-stock/v1/quotations/inquire-time-itemchartprice";
    
    // TR ID 상수
    public static final String DAILY_CHART_TR_ID = "FHKST03010100";
    public static final String MINUTE_CHART_TR_ID = "FHKST03010200";
    public static final String STOCK_ASKING_PRICE_TR_ID = "H0STASP0";
    public static final String STOCK_CONCLUSION_TR_ID = "H0STCNT0";
    
    // WebSocket URL
    public static final String WEBSOCKET_URL = "ws://ops.koreainvestment.com:21000";
    
    // 기타 상수
    public static final String CUSTOMER_TYPE_PERSONAL = "P"; // 개인고객
    public static final String MARKET_CODE_KOSPI = "J"; // 코스피
    
    // API 이름
    public static final String STOCK_CURRENT_PRICE_NAME = "주식현재가 시세";
}
