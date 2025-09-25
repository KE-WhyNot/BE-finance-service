package com.youthfi.finance.domain.stock.infra;

import com.youthfi.finance.global.config.properties.KisApiProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class StockWebSocketApprovalKeyService {
    private final KisApiProperties kisApiProperties;
    private final RestTemplate restTemplate;
    private final Map<String, String> approvalKeyMap = new HashMap<>();

    public StockWebSocketApprovalKeyService(KisApiProperties kisApiProperties, RestTemplate restTemplate) {
        this.kisApiProperties = kisApiProperties;
        this.restTemplate = restTemplate;
    }

    public synchronized String getApprovalKey(String appkey, String appsecret) {
        if (!approvalKeyMap.containsKey(appkey)) {
            String url = "https://openapi.koreainvestment.com:9443/oauth2/Approval";
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
                    approvalKeyMap.put(appkey, approvalKey.toString());
                } else {
                    throw new RuntimeException("approval_key not found in response");
                }
            } else {
                throw new RuntimeException("WebSocket approval key 발급 실패: " + response.getStatusCode());
            }
        }
        return approvalKeyMap.get(appkey);
    }
}