package com.youthfi.finance.domain.stock.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youthfi.finance.domain.stock.application.dto.response.StockWebSocketResponse;
import com.youthfi.finance.domain.stock.infra.StockWebSocketApprovalKeyManager;
import com.youthfi.finance.domain.stock.infra.StockWebSocketClient;
import com.youthfi.finance.global.config.properties.KisApiEndpoints;
import com.youthfi.finance.global.config.properties.KisApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
}
