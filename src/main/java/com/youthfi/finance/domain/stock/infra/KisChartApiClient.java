package com.youthfi.finance.domain.stock.infra;

import com.youthfi.finance.domain.stock.application.dto.response.CandleDataResponse;
import com.youthfi.finance.domain.stock.application.dto.response.ChartDataResponse;
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
 * KIS API 차트 데이터 조회 클라이언트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KisChartApiClient {

    private final RestTemplate restTemplate;
    private final KisTokenService kisTokenService;
    private final KisApiProperties kisApiProperties;
    
    
    /**
     * 일봉 데이터 조회 (60일)
     */
    public ChartDataResponse getDailyChart(String stockCode, int days) {
        try {
            String appkey = kisApiProperties.getKeys().get(0).getAppkey();
            String appsecret = kisApiProperties.getKeys().get(0).getAppsecret();
            String accessToken = kisTokenService.getValidToken(appkey, appsecret);
            
            HttpHeaders headers = createHeaders(accessToken, appkey, appsecret, KisApiEndpoints.DAILY_CHART_TR_ID);
            String url = buildChartUrl(stockCode, "D", days);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class);
            
            return parseChartResponse(stockCode, "1d", String.valueOf(days) + "d", response);
            
        } catch (Exception e) {
            log.error("일봉 데이터 조회 실패: stockCode={}, days={}", stockCode, days, e);
            throw StockException.kisApiConnectionFailed(e);
        }
    }
    
    /**
     * 월봉 데이터 조회 (5년)
     */
    public ChartDataResponse getMonthlyChart(String stockCode, int years) {
        try {
            String appkey = kisApiProperties.getKeys().get(0).getAppkey();
            String appsecret = kisApiProperties.getKeys().get(0).getAppsecret();
            String accessToken = kisTokenService.getValidToken(appkey, appsecret);
            
            HttpHeaders headers = createHeaders(accessToken, appkey, appsecret, KisApiEndpoints.DAILY_CHART_TR_ID);
            String url = buildChartUrl(stockCode, "M", years * 12);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class);
            
            return parseChartResponse(stockCode, "1m", String.valueOf(years) + "y", response);
            
        } catch (Exception e) {
            log.error("월봉 데이터 조회 실패: stockCode={}, years={}", stockCode, years, e);
            throw StockException.kisApiConnectionFailed(e);
        }
    }
    
    /**
     * 연봉 데이터 조회 (10년)
     */
    public ChartDataResponse getYearlyChart(String stockCode, int years) {
        try {
            String appkey = kisApiProperties.getKeys().get(0).getAppkey();
            String appsecret = kisApiProperties.getKeys().get(0).getAppsecret();
            String accessToken = kisTokenService.getValidToken(appkey, appsecret);
            
            HttpHeaders headers = createHeaders(accessToken, appkey, appsecret, KisApiEndpoints.DAILY_CHART_TR_ID);
            String url = buildChartUrl(stockCode, "Y", years);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class);
            
            return parseChartResponse(stockCode, "1y", String.valueOf(years) + "y", response);
            
        } catch (Exception e) {
            log.error("연봉 데이터 조회 실패: stockCode={}, years={}", stockCode, years, e);
            throw StockException.kisApiConnectionFailed(e);
        }
    }
    
    /**
     * 분봉 데이터 조회 (당일)
     */

    public ChartDataResponse getMinuteChart(String stockCode) {
        try {
            String appkey = kisApiProperties.getKeys().get(0).getAppkey();
            String appsecret = kisApiProperties.getKeys().get(0).getAppsecret();
            String accessToken = kisTokenService.getValidToken(appkey, appsecret);
            
            HttpHeaders headers = createHeaders(accessToken, appkey, appsecret, KisApiEndpoints.MINUTE_CHART_TR_ID);
            String url = buildMinuteChartUrl(stockCode);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class);
            
            return parseChartResponse(stockCode, "1min", "today", response);
            
        } catch (Exception e) {
            log.error("분봉 데이터 조회 실패: stockCode={}", stockCode, e);
            throw StockException.kisApiConnectionFailed(e);
        }
    }
    
    private HttpHeaders createHeaders(String accessToken, String appkey, String appsecret, String trId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + accessToken);
        headers.set("appkey", appkey);
        headers.set("appsecret", appsecret);
        headers.set("tr_id", trId);
        headers.set("custtype", KisApiEndpoints.CUSTOMER_TYPE_PERSONAL); // 개인고객
        return headers;
    }
    
    private String buildChartUrl(String stockCode, String period, int count) {
        // 기간별 일 단위 변환
        int daysOffset;
        switch (period) {
            case "Y": // 연봉: 충분한 기간 요청 (20년치)
                daysOffset = 20 * 365; // 20년치 요청
                break;
            case "M": // 월봉: 월 단위를 일 단위로 변환 (5년 = 60개월이므로 충분한 일수 요청)
                daysOffset = count * 30; // 5년 = 150일로 요청
                break;
            case "D": // 일봉: 그대로 사용
            default:
                daysOffset = count;
                break;
        }
        
        return UriComponentsBuilder
            .fromHttpUrl(KisApiEndpoints.REAL_BASE_URL + KisApiEndpoints.DAILY_CHART)
            .queryParam("fid_cond_mrkt_div_code", KisApiEndpoints.MARKET_CODE_KOSPI)
            .queryParam("fid_input_iscd", stockCode)
            .queryParam("fid_input_date_1", getDateString(-daysOffset))
            .queryParam("fid_input_date_2", getDateString(0))
            .queryParam("fid_period_div_code", period)
            .queryParam("fid_org_adj_prc", "1")
            .toUriString();
    }
    
    private String buildMinuteChartUrl(String stockCode) {
        return UriComponentsBuilder
            .fromHttpUrl(KisApiEndpoints.REAL_BASE_URL + KisApiEndpoints.MINUTE_CHART)
            .queryParam("fid_cond_mrkt_div_code", KisApiEndpoints.MARKET_CODE_KOSPI)
            .queryParam("fid_input_iscd", stockCode)
            .queryParam("fid_input_hour_1", "090000")
            .queryParam("fid_pw_data_incu_yn", "Y")
            .queryParam("fid_etc_cls_code", "0")
            .toUriString();
    }
    
    private String getDateString(int daysOffset) {
        return LocalDate.now().plusDays(daysOffset).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
    
    private String getTodayString() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
    
    @SuppressWarnings("unchecked")
    private ChartDataResponse parseChartResponse(String stockCode, String period, String range, ResponseEntity<Map> response) {
        try {
            Map<String, Object> body = response.getBody();
            log.info("KIS API 전체 응답: {}", body);
            
            if (body == null || !"0".equals(body.get("rt_cd"))) {
                log.warn("KIS API 응답 오류: {}", body);
                return ChartDataResponse.empty(stockCode, period, range);
            }
            
            // KIS API 응답 구조: output1, output2가 최상위 레벨에 있음
            List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("output2");
            log.info("KIS API output2 (items): {}", items);
            
            if (items == null || items.isEmpty()) {
                log.warn("output2가 null이거나 비어있습니다.");
                return ChartDataResponse.empty(stockCode, period, range);
            }
            
            List<CandleDataResponse> candles = new ArrayList<>();
            if ("1min".equals(period)) {
                // 분봉: cntg_vol 우선, 없으면 acml_vol 차분으로 계산
                // 시간순 정렬을 위해 과거→현재 순으로 처리
                List<Map<String, Object>> sortedItems = new ArrayList<>(items);
                sortedItems.sort((a, b) -> {
                    String timeA = asString(a.get("stck_cntg_hour"));
                    String timeB = asString(b.get("stck_cntg_hour"));
                    return timeA.compareTo(timeB);
                });
                
                Long prevAcmlVol = null;
                for (Map<String, Object> item : sortedItems) {
                    try {
                        String dateStr = asString(item.get("stck_bsop_date"));
                        String timeStr = asString(item.get("stck_cntg_hour")); // 체결시간 (HHMM)
                        String openStr = asString(item.get("stck_oprc"));
                        String highStr = asString(item.get("stck_hgpr"));
                        String lowStr = asString(item.get("stck_lwpr"));
                        String closeStr = asString(item.get("stck_prpr"));

                        String cntgVolStr = asString(item.get("cntg_vol"));
                        String acmlVolStr = asString(item.get("acml_vol"));

                        Long volumeVal;
                        Long cntgVol = parseLongOrZero(cntgVolStr);
                        if (cntgVol != null && cntgVol > 0) {
                            // cntg_vol이 있으면 그대로 사용
                            volumeVal = cntgVol;
                        } else {
                            // cntg_vol이 없으면 acml_vol 차분으로 계산
                            Long acmlVol = parseLongOrZero(acmlVolStr);
                            if (acmlVol != null && prevAcmlVol != null && acmlVol >= prevAcmlVol) {
                                volumeVal = acmlVol - prevAcmlVol;
                            } else {
                                volumeVal = 0L;
                            }
                        }
                        
                        // 다음 차분 계산을 위해 현재 acml_vol 저장
                        Long currentAcmlVol = parseLongOrZero(acmlVolStr);
                        if (currentAcmlVol != null) {
                            prevAcmlVol = currentAcmlVol;
                        }

                        CandleDataResponse candle = CandleDataResponse.of(
                            dateStr,
                            timeStr, // 시간 정보 추가
                            openStr,
                            highStr,
                            lowStr,
                            closeStr,
                            String.valueOf(volumeVal)
                        );
                        candles.add(candle);
                    } catch (Exception e) {
                        log.warn("캔들 데이터 파싱 실패: {}", item, e);
                    }
                }
            } else {
                // 일/월/연: 기존 로직 유지 (volume=acml_vol)
                for (Map<String, Object> item : items) {
                    try {
                        String dateStr = asString(item.get("stck_bsop_date"));
                        String openStr = firstNonNull(asString(item.get("stck_oprc")), asString(item.get("stck_prpr")));
                        String highStr = asString(item.get("stck_hgpr"));
                        String lowStr = asString(item.get("stck_lwpr"));
                        String closeStr = firstNonNull(asString(item.get("stck_clpr")), asString(item.get("stck_prpr")));
                        String volStr = asString(item.get("acml_vol"));

                        CandleDataResponse candle = CandleDataResponse.of(
                            dateStr,
                            openStr,
                            highStr,
                            lowStr,
                            closeStr,
                            volStr
                        );
                        candles.add(candle);
                    } catch (Exception e) {
                        log.warn("캔들 데이터 파싱 실패: {}", item, e);
                    }
                }
                
                // 연봉의 경우 최신 N개만 반환 (요청한 연수만큼)
                if ("1y".equals(period)) {
                    int requestedYears = extractYears(range);
                    if (candles.size() > requestedYears) {
                        candles = new ArrayList<>(candles.subList(0, Math.min(requestedYears, candles.size())));
                    }
                }
            }
            
            return new ChartDataResponse(stockCode, period, range, candles, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
        } catch (Exception e) {
            log.error("차트 응답 파싱 실패", e);
            throw StockException.kisApiResponseError(e);
        }
    }

    private String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private String firstNonNull(String a, String b) {
        return a != null && !a.isEmpty() ? a : (b != null ? b : null);
    }

    private Long parseLongOrZero(String value) {
        if (value == null || value.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
    
    private int extractYears(String range) {
        if (range.endsWith("y")) {
            return Integer.parseInt(range.substring(0, range.length() - 1));
        }
        return 10; // 기본값
    }
}
