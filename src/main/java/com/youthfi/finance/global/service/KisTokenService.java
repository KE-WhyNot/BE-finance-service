package com.youthfi.finance.global.service;

import com.youthfi.finance.global.config.RedisConstants;
import com.youthfi.finance.global.config.properties.KisApiEndpoints;
import com.youthfi.finance.global.config.properties.KisApiProperties;
import com.youthfi.finance.global.exception.StockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KisTokenService {
    private final KisApiProperties kisApiProperties;
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate;


    



    public KisTokenService(KisApiProperties kisApiProperties, RestTemplate restTemplate, RedisTemplate<String, String> redisTemplate) {
        this.kisApiProperties = kisApiProperties;
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }


    
    public synchronized String getValidToken(String appkey, String appsecret) {
        String tokenKey = RedisConstants.buildKisTokenKey(appkey);
        String token = redisTemplate.opsForValue().get(tokenKey);
        
        if (token == null) {
            log.info("새로운 토큰 발급: {}", appkey);
            fetchToken(appkey, appsecret);
            token = redisTemplate.opsForValue().get(tokenKey);
        } else if (isTokenExpiringSoon(appkey)) {
            log.info("토큰 갱신: {} (만료 1시간 전)", appkey);
            fetchToken(appkey, appsecret);
            token = redisTemplate.opsForValue().get(tokenKey);
        } else {
            log.debug("기존 토큰 재사용: {}", appkey);
        }
        return token;
    }



    public synchronized void fetchToken(String appkey, String appsecret) {
        String url = KisApiEndpoints.REAL_BASE_URL + KisApiEndpoints.TOKEN_ISSUANCE;
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
            
            // Redis에 토큰과 만료시간 저장 (24시간 TTL)
            String tokenKey = RedisConstants.buildKisTokenKey(appkey);
            String expiryKey = RedisConstants.buildKisExpiryKey(appkey);
            
            redisTemplate.opsForValue().set(tokenKey, token, Duration.ofHours(24));
            redisTemplate.opsForValue().set(expiryKey, expiredAt.toString(), Duration.ofHours(24));
        } else {
            throw StockException.kisApiConnectionFailed(new RuntimeException("토큰 발급 실패: " + response.getStatusCode()));
        }
    }



    public boolean isTokenExpiringSoon(String appkey) {
        String expiryKey = RedisConstants.buildKisExpiryKey(appkey);
        String expiryStr = redisTemplate.opsForValue().get(expiryKey);
        
        if (expiryStr == null) {
            return true; // 토큰이 없으면 갱신 필요
        }
        
        try {
            LocalDateTime tokenExpiredAt = LocalDateTime.parse(expiryStr);
            // 24시간 토큰이므로 만료 1시간 전에 갱신 (23시간 후)
            return tokenExpiredAt.minusHours(1).isBefore(LocalDateTime.now());
        } catch (Exception e) {
            return true; // 파싱 실패시 갱신 필요
        }
    }

    

    /**
     * 토큰 상태 모든 정보 조회
     */
    public Map<String, Object> getTokenStatus(String appkey) {
        Map<String, Object> status = new HashMap<>();
        String tokenKey = RedisConstants.buildKisTokenKey(appkey);
        String expiryKey = RedisConstants.buildKisExpiryKey(appkey);
        
        String token = redisTemplate.opsForValue().get(tokenKey);
        String expiryStr = redisTemplate.opsForValue().get(expiryKey);
        
        status.put("hasToken", token != null);
        status.put("expiredAt", expiryStr);
        status.put("isExpiringSoon", isTokenExpiringSoon(appkey));
        return status;
    }



    /**
     * 모든 토큰 상태 조회
     */
    public Map<String, Map<String, Object>> getAllTokenStatus() {
        Map<String, Map<String, Object>> allStatus = new HashMap<>();
        
        // Redis에서 모든 토큰 키 조회
        String pattern = RedisConstants.KIS_TOKEN_PREFIX + "*";
        var keys = redisTemplate.keys(pattern);
        
        if (keys != null) {
            for (String key : keys) {
                String appkey = key.substring(RedisConstants.KIS_TOKEN_PREFIX.length());
                allStatus.put(appkey, getTokenStatus(appkey));
            }
        }
        
        return allStatus;
    }
}