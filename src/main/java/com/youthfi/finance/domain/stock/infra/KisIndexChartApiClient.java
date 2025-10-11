package com.youthfi.finance.domain.stock.infra;

import com.youthfi.finance.domain.stock.application.dto.response.IndexChartDataResponse;
import com.youthfi.finance.domain.stock.application.dto.response.IndexCandleDataResponse;
import com.youthfi.finance.global.config.properties.KisApiEndpoints;
import com.youthfi.finance.global.config.properties.KisApiProperties;
import com.youthfi.finance.global.service.KisTokenService;
import com.youthfi.finance.global.exception.StockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * KIS API 지수 차트 데이터 조회 클라이언트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KisIndexChartApiClient {

    private final RestTemplate restTemplate;
    private final KisTokenService kisTokenService;
    private final KisApiProperties kisApiProperties;
    
    private static final String INDEX_CHART_TR_ID = "FHKUP03500100";
    
    /**
     * 일봉 데이터 조회 (30일)
     */
    public IndexChartDataResponse getDailyIndexChart(String indexCode) {
        try {
            String appkey = kisApiProperties.getKeys().get(0).getAppkey();
            String appsecret = kisApiProperties.getKeys().get(0).getAppsecret();
            String accessToken = kisTokenService.getValidToken(appkey, appsecret);
            
            HttpHeaders headers = createHeaders(accessToken, appkey, appsecret, INDEX_CHART_TR_ID);
            String url = buildIndexChartUrl(indexCode, "D", 30);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class);
            
            return parseIndexChartResponse(indexCode, "D", "30d", response);
            
        } catch (Exception e) {
            log.error("지수 일봉 데이터 조회 실패: indexCode={}", indexCode, e);
            throw StockException.kisApiConnectionFailed(e);
        }
    }
    
    /**
     * 주봉 데이터 조회 (30주)
     */
    public IndexChartDataResponse getWeeklyIndexChart(String indexCode) {
        try {
            String appkey = kisApiProperties.getKeys().get(0).getAppkey();
            String appsecret = kisApiProperties.getKeys().get(0).getAppsecret();
            String accessToken = kisTokenService.getValidToken(appkey, appsecret);
            
            HttpHeaders headers = createHeaders(accessToken, appkey, appsecret, INDEX_CHART_TR_ID);
            String url = buildIndexChartUrl(indexCode, "W", 30);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class);
            
            return parseIndexChartResponse(indexCode, "W", "30w", response);
            
        } catch (Exception e) {
            log.error("지수 주봉 데이터 조회 실패: indexCode={}", indexCode, e);
            throw StockException.kisApiConnectionFailed(e);
        }
    }
    
    /**
     * 월봉 데이터 조회 (30개월)
     */
    public IndexChartDataResponse getMonthlyIndexChart(String indexCode) {
        try {
            String appkey = kisApiProperties.getKeys().get(0).getAppkey();
            String appsecret = kisApiProperties.getKeys().get(0).getAppsecret();
            String accessToken = kisTokenService.getValidToken(appkey, appsecret);
            
            HttpHeaders headers = createHeaders(accessToken, appkey, appsecret, INDEX_CHART_TR_ID);
            String url = buildIndexChartUrl(indexCode, "M", 30);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class);
            
            return parseIndexChartResponse(indexCode, "M", "30m", response);
            
        } catch (Exception e) {
            log.error("지수 월봉 데이터 조회 실패: indexCode={}", indexCode, e);
            throw StockException.kisApiConnectionFailed(e);
        }
    }
    
    /**
     * HTTP 헤더 생성
     */
    private HttpHeaders createHeaders(String accessToken, String appkey, String appsecret, String trId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + accessToken);
        headers.set("appkey", appkey);
        headers.set("appsecret", appsecret);
        headers.set("tr_id", trId);
        return headers;
    }
    
    /**
     * 지수 차트 URL 생성
     */
    private String buildIndexChartUrl(String indexCode, String period, int count) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(count * getDaysPerPeriod(period));
        
        return UriComponentsBuilder.fromHttpUrl("https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/quotations/inquire-daily-indexchartprice")
                .queryParam("FID_COND_MRKT_DIV_CODE", "U")
                .queryParam("FID_INPUT_ISCD", indexCode)
                .queryParam("FID_INPUT_DATE_1", startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .queryParam("FID_INPUT_DATE_2", endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .queryParam("FID_PERIOD_DIV_CODE", period)
                .build()
                .toUriString();
    }
    
    /**
     * 기간별 일수 계산
     */
    private int getDaysPerPeriod(String period) {
        return switch (period) {
            case "D" -> 1;
            case "W" -> 7;
            case "M" -> 30;
            default -> 1;
        };
    }
    
    /**
     * 지수 차트 응답 파싱
     */
    @SuppressWarnings("unchecked")
    private IndexChartDataResponse parseIndexChartResponse(String indexCode, String period, String range, ResponseEntity<Map> response) {
        try {
            Map<String, Object> body = response.getBody();
            log.info("KIS API 지수 차트 전체 응답: {}", body);
            
            if (body == null || !"0".equals(body.get("rt_cd"))) {
                log.warn("KIS API 지수 차트 응답 오류: {}", body);
                return IndexChartDataResponse.empty(indexCode, period, range);
            }
            
            // KIS API 응답 구조: output2가 최상위 레벨에 있음
            List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("output2");
            log.info("KIS API 지수 차트 output2 (items): {}", items);
            
            if (items == null || items.isEmpty()) {
                log.warn("지수 차트 output2가 null이거나 비어있습니다.");
                return IndexChartDataResponse.empty(indexCode, period, range);
            }
            
            List<IndexCandleDataResponse> candles = new ArrayList<>();
            for (Map<String, Object> item : items) {
                try {
                    String dateStr = asString(item.get("stck_bsop_date"));
                    String currentPriceStr = asString(item.get("bstp_nmix_prpr"));
                    String highPriceStr = asString(item.get("bstp_nmix_hgpr"));
                    String lowPriceStr = asString(item.get("bstp_nmix_lwpr"));
                    String volumeStr = asString(item.get("acml_vol"));

                    IndexCandleDataResponse candle = IndexCandleDataResponse.of(
                        dateStr,
                        currentPriceStr,
                        highPriceStr,
                        lowPriceStr,
                        volumeStr
                    );
                    candles.add(candle);
                } catch (Exception e) {
                    log.warn("지수 캔들 데이터 파싱 실패: {}", item, e);
                }
            }
            
            return new IndexChartDataResponse(
                indexCode, 
                period, 
                range, 
                candles, 
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            
        } catch (Exception e) {
            log.error("지수 차트 응답 파싱 실패", e);
            throw StockException.kisApiResponseError(e);
        }
    }

    /**
     * Object를 String으로 변환
     */
    private String asString(Object value) {
        return value != null ? value.toString() : "";
    }
}
