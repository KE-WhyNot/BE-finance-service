package com.youthfi.finance.domain.stock.infra;

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
    
    // 예탁원정보(배당일정) API - 실제 KIS API 엔드포인트
    public static final String DIVIDEND_SCHEDULE = "/uapi/domestic-stock/v1/ksdinfo/dividend";
    public static final String DIVIDEND_SCHEDULE_TR_ID = "HHKDB669102C0";
    
    // API 카테고리
    public static final String DOMESTIC_STOCK = "국내주식";
    
    // API 이름
    public static final String STOCK_CURRENT_PRICE_NAME = "주식현재가 시세";
    public static final String DIVIDEND_SCHEDULE_NAME = "예탁원정보(배당일정)";
}
