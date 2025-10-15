package com.youthfi.finance.domain.stock.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youthfi.finance.domain.stock.application.dto.response.StockWebSocketResponse;
import com.youthfi.finance.domain.stock.infra.StockWebSocketApprovalKeyManager;
import com.youthfi.finance.domain.stock.infra.StockWebSocketClient;
import com.youthfi.finance.global.config.properties.KisApiEndpoints;
import com.youthfi.finance.global.config.properties.KisApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import com.youthfi.finance.global.util.StockFrontendWebSocketHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockWebSocketService {

    private final StockWebSocketApprovalKeyManager approvalKeyManager;
    private final KisApiProperties kisApiProperties;
    private final List<StockWebSocketClient> clients = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 지정된 종목들에 대한 WebSocket 시작
     */
    public void startWebSocketsForAllKeysAndStocks(List<String> stockCodes) {
        // 기존 연결 정리
        stopAllWebSockets();
        
        List<KisApiProperties.KisKey> keys = kisApiProperties.getKeys();
        String[] trIds = {KisApiEndpoints.STOCK_CONCLUSION_TR_ID}; // 체결가만 구독
        int maxPerSession = 42; // trId 1개면 42종목씩
        int idx = 0;
        
        for (KisApiProperties.KisKey key : keys) {
            String appkey = key.getAppkey();
            String appsecret = key.getAppsecret();
            String approvalKey = approvalKeyManager.getApprovalKey(appkey, appsecret);
            
            for (String trId : trIds) {
                List<String> stocksForThisSession = new ArrayList<>();
                for (int i = 0; i < maxPerSession && idx < stockCodes.size(); i++, idx++) {
                    stocksForThisSession.add(stockCodes.get(idx));
                }
                
                if (!stocksForThisSession.isEmpty()) {
                    try {
                        URI uri = new URI(KisApiEndpoints.WEBSOCKET_URL);
                        StockWebSocketClient client = new StockWebSocketClient(
                                uri, appkey, approvalKey, trId, stocksForThisSession,
                                this::handleIncomingMessage
                        );
                        client.connect();
                        clients.add(client);
                        
                        // 연결 간격을 두어 "ALREADY IN USE" 오류 방지
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleIncomingMessage(StockWebSocketResponse dto) {
        try {
            String json = objectMapper.writeValueAsString(dto);
            StockFrontendWebSocketHandler.broadcast(json);
        } catch (Exception e) {
            
        }
    }

    /**
     * 모든 WebSocket 연결 종료 
     */
    public void stopAllWebSockets() {
        for (StockWebSocketClient client : clients) {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clients.clear();
    }

    /**
     * WebSocket 연결 상태 확인 
     */
    public int getActiveConnectionCount() {
        return clients.size();
    }

    /**
     * 주기적으로 모든 세션을 롤링 재시작 (10분)
     * 기존 구독 종목 리스트를 그대로 사용하고, approval key는 최신으로 갱신
     */
    @Scheduled(fixedDelay = 600_000L, initialDelay = 600_000L)
    public void rollingRestartWebSockets() {
        if (clients.isEmpty()) {
            return;
        }
        List<StockWebSocketClient> snapshot = new ArrayList<>(clients);
        clients.clear();
        for (StockWebSocketClient oldClient : snapshot) {
            try {
                oldClient.close();
            } catch (Exception ignored) {}
            try {
                String appkey = oldClient.getAppkey();
                String appsecret = kisApiProperties.getKeys().stream()
                        .filter(k -> k.getAppkey().equals(appkey))
                        .findFirst()
                        .map(KisApiProperties.KisKey::getAppsecret)
                        .orElse("");
                String approvalKey = approvalKeyManager.getApprovalKey(appkey, appsecret);
                URI uri = new URI(KisApiEndpoints.WEBSOCKET_URL);
                StockWebSocketClient newClient = new StockWebSocketClient(
                        uri, appkey, approvalKey, oldClient.getTrId(), oldClient.getTrKeys(),
                        this::handleIncomingMessage
                );
                newClient.connect();
                clients.add(newClient);
                Thread.sleep(1000);
            } catch (Exception e) {
                // 개별 세션 실패는 다음 루프로 계속
            }
        }
    }
}
