package com.youthfi.finance.domain.stock.application.usecase;

import com.youthfi.finance.domain.stock.infra.StockWebSocketClient;
import com.youthfi.finance.domain.stock.infra.StockWebSocketApprovalKeyService;
import org.springframework.stereotype.Service;
import java.net.URI;

@Service
public class StockRealtimeUseCase {
    private StockWebSocketClient client;
    private final StockWebSocketApprovalKeyService approvalKeyService;

    public StockRealtimeUseCase(StockWebSocketApprovalKeyService approvalKeyService) {
        this.approvalKeyService = approvalKeyService;
    }

    public void startWebSocket() {
        if (client != null && client.isOpen()) {
            return;
        }
        try {
            String approvalKey = approvalKeyService.getApprovalKey();
            String trKey = "005930";  // 예: 삼성전자
            URI uri = new URI("ws://ops.koreainvestment.com:21000");
            client = new StockWebSocketClient(uri, approvalKey, trKey);
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}