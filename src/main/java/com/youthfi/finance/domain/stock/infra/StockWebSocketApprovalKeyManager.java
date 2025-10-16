package com.youthfi.finance.domain.stock.infra;

import com.youthfi.finance.global.config.properties.KisApiEndpoints;
import com.youthfi.finance.global.config.properties.KisApiProperties;
import com.youthfi.finance.global.config.RedisConstants;
import com.youthfi.finance.global.exception.StockException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Component
public class StockWebSocketApprovalKeyManager {
    private final KisApiProperties kisApiProperties;
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    public StockWebSocketApprovalKeyManager(KisApiProperties kisApiProperties, RestTemplate restTemplate, RedisTemplate<String, String> redisTemplate) {
        this.kisApiProperties = kisApiProperties;
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    public synchronized String getApprovalKey(String appkey, String appsecret) {
        String approvalKeyRedisKey = RedisConstants.buildKisWsApprovalKey(appkey);
        String expiryRedisKey = RedisConstants.buildKisWsExpiryKey(appkey);

        String cached = redisTemplate.opsForValue().get(approvalKeyRedisKey);
        String expiryStr = redisTemplate.opsForValue().get(expiryRedisKey);

        if (cached == null || isExpiringSoon(expiryStr)) {
            String url = KisApiEndpoints.REAL_BASE_URL + KisApiEndpoints.WEBSOCKET_APPROVAL;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("grant_type", "client_credentials");
            body.put("appkey", appkey);
            body.put("secretkey", appsecret);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object approvalKey = response.getBody().get("approval_key");
                if (approvalKey != null) {
                    String key = approvalKey.toString();
                    redisTemplate.opsForValue().set(approvalKeyRedisKey, key);
                    redisTemplate.opsForValue().set(expiryRedisKey, java.time.LocalDateTime.now().plusHours(24).toString());
                } else {
                    throw StockException.kisApiResponseError(new RuntimeException("approval_key not found in response"));
                }
            } else {
                throw StockException.kisApiConnectionFailed(new RuntimeException("WebSocket approval key 발급 실패: " + response.getStatusCode()));
            }
            return redisTemplate.opsForValue().get(approvalKeyRedisKey);
        }
        return cached;
    }

    private boolean isExpiringSoon(String expiryStr) {
        if (expiryStr == null) return true;
        try {
            java.time.LocalDateTime expiredAt = java.time.LocalDateTime.parse(expiryStr);
            return expiredAt.minusHours(1).isBefore(java.time.LocalDateTime.now());
        } catch (Exception e) {
            return true;
        }
    }
}