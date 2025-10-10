package com.youthfi.finance.domain.stock.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youthfi.finance.domain.stock.domain.entity.Stock;
import com.youthfi.finance.domain.stock.application.dto.response.StockWebSocketResponse;
import com.youthfi.finance.domain.stock.domain.repository.StockRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockWebSocketService {

    private final StockWebSocketApprovalKeyManager approvalKeyManager;
    private final KisApiProperties kisApiProperties;
    private final StockRepository stockRepository;
    private final List<StockWebSocketClient> clients = new ArrayList<>();
    private final ObjectMapper objectMapper;

    /**
     * DB에서 모든 종목 조회
     */
    public List<String> getAllStockCodes() {
        List<Stock> stocks = stockRepository.findAllByOrderByStockId();
        return stocks.stream()
                .map(Stock::getStockId)
                .collect(Collectors.toList());
    }

    /**
     * 모든 키와 종목에 대한 WebSocket 시작
     */
    public void startWebSocketsForAllKeysAndStocks(List<String> allStocks) {
        // 기존 연결 정리
        stopAllWebSockets();
        
        List<KisApiProperties.KisKey> keys = kisApiProperties.getKeys();
        String[] trIds = {KisApiEndpoints.STOCK_ASKING_PRICE_TR_ID, KisApiEndpoints.STOCK_CONCLUSION_TR_ID};
        int maxPerSession = 21; // trId 2개면 21종목씩
        int idx = 0;
        
        for (KisApiProperties.KisKey key : keys) {
            String appkey = key.getAppkey();
            String appsecret = key.getAppsecret();
            String approvalKey = approvalKeyManager.getApprovalKey(appkey, appsecret);
            
            for (String trId : trIds) {
                List<String> stocksForThisSession = new ArrayList<>();
                for (int i = 0; i < maxPerSession && idx < allStocks.size(); i++, idx++) {
                    stocksForThisSession.add(allStocks.get(idx));
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
