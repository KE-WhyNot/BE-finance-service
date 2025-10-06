package com.youthfi.finance.domain.stock.infra;

import com.youthfi.finance.global.config.properties.KisApiProperties;
import com.youthfi.finance.global.config.properties.KisApiEndpoints;
import com.youthfi.finance.global.service.KisTokenService;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class DividendScheduleApiClient {
    
    private final KisTokenService kisTokenService;
    private final RestTemplate restTemplate;
    private final KisApiProperties kisApiProperties;
    
    public DividendScheduleApiClient(KisTokenService kisTokenService, 
                                   RestTemplate restTemplate, 
                                   KisApiProperties kisApiProperties) {
        this.kisTokenService = kisTokenService;
        this.restTemplate = restTemplate;
        this.kisApiProperties = kisApiProperties;
    }
    
    /**
     * 예탁원정보(배당일정) 조회
     * @param stockCode 종목코드 (ex: 005930)
     * @return 배당일정 데이터
     */
    public Map<String, Object> getDividendSchedule(String stockCode) {
        try {
            // 토큰 발급
            String appkey = kisApiProperties.getKeys().get(0).getAppkey();
            String appsecret = kisApiProperties.getKeys().get(0).getAppsecret();
            String token = kisTokenService.getValidToken(appkey, appsecret);
            
            // URL 구성 - KIS API 배당일정 파라미터 추가
            String url = UriComponentsBuilder
                    .fromHttpUrl(KisApiEndpoints.REAL_BASE_URL + KisApiEndpoints.DIVIDEND_SCHEDULE)
                    .queryParam("SHT_CD", stockCode)
                    .queryParam("GB1", "0") // 배당구분코드 (0: 전체)
                    .queryParam("F_DT", "20240101") // 조회시작일
                    .queryParam("T_DT", "20241231") // 조회종료일
                    .toUriString();
            
            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("authorization", "Bearer " + token);
            headers.set("appkey", appkey);
            headers.set("appsecret", appsecret);
            headers.set("tr_id", KisApiEndpoints.DIVIDEND_SCHEDULE_TR_ID);
            headers.set("custtype", "P"); // 개인고객
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            
            // API 호출
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, 
                    HttpMethod.GET, 
                    request, 
                    Map.class
            );
            
            return response.getBody();
            
        } catch (Exception e) {
            // 로그 출력 및 기본 응답 반환
            System.err.println("배당일정 조회 오류: " + e.getMessage());
            e.printStackTrace();
            
            // 기본 응답 반환
            return Map.of(
                "error", "KIS API 호출 실패",
                "message", e.getMessage(),
                "stockCode", stockCode
            );
        }
    }
}
