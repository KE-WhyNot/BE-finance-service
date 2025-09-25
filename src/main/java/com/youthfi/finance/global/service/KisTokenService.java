package com.youthfi.finance.global.service;

import com.youthfi.finance.global.config.properties.KisApiProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class KisTokenService {
    private final KisApiProperties kisApiProperties;
    private final RestTemplate restTemplate;

    // appkey별 토큰/만료 캐싱
    private final Map<String, String> accessTokenMap = new HashMap<>();
    private final Map<String, LocalDateTime> tokenExpiredAtMap = new HashMap<>();

    public KisTokenService(KisApiProperties kisApiProperties, RestTemplate restTemplate) {
        this.kisApiProperties = kisApiProperties;
        this.restTemplate = restTemplate;
    }

    public synchronized void fetchToken(String appkey, String appsecret) {
        String url = "https://openapi.koreainvestment.com:9443/oauth2/tokenP";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "client_credentials");
        body.put("appkey", appkey);
        body.put("appsecret", appsecret);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> respBody = response.getBody();
            String token = (String) respBody.get("access_token");
            String expiredAtStr = (String) respBody.get("access_token_token_expired");
            LocalDateTime expiredAt = LocalDateTime.parse(expiredAtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            accessTokenMap.put(appkey, token);
            tokenExpiredAtMap.put(appkey, expiredAt);
        } else {
            throw new RuntimeException("토큰 발급 실패: " + response.getStatusCode());
        }
    }

    public boolean isTokenExpiringSoon(String appkey) {
        LocalDateTime tokenExpiredAt = tokenExpiredAtMap.get(appkey);
        return tokenExpiredAt == null || tokenExpiredAt.minusMinutes(10).isBefore(LocalDateTime.now());
    }

    public synchronized String getValidToken(String appkey, String appsecret) {
        if (!accessTokenMap.containsKey(appkey) || isTokenExpiringSoon(appkey)) {
            fetchToken(appkey, appsecret);
        }
        return accessTokenMap.get(appkey);
    }
}