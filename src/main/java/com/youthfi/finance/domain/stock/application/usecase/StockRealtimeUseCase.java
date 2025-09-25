package com.youthfi.finance.domain.stock.application.usecase;

import com.youthfi.finance.domain.stock.infra.StockWebSocketClient;
import com.youthfi.finance.domain.stock.infra.StockWebSocketApprovalKeyService;
import com.youthfi.finance.global.config.properties.KisApiProperties;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockRealtimeUseCase {
    private final StockWebSocketApprovalKeyService approvalKeyService;
    private final KisApiProperties kisApiProperties;
    private final List<StockWebSocketClient> clients = new ArrayList<>();

    public StockRealtimeUseCase(StockWebSocketApprovalKeyService approvalKeyService, KisApiProperties kisApiProperties) {
        this.approvalKeyService = approvalKeyService;
        this.kisApiProperties = kisApiProperties;
    }

    public void startWebSocketsForAllKeysAndStocks(List<String> allStocks) {
        // 예시: 2개 appkey, 2개 trId, 21개 종목씩 분배
        List<KisApiProperties.KisKey> keys = kisApiProperties.getKeys();
        String[] trIds = {"H0STASP0", "H0STCNT0"};
        int maxPerSession = 21; // trId 2개면 21종목씩
        int idx = 0;
        for (KisApiProperties.KisKey key : keys) {
            String appkey = key.getAppkey();
            String appsecret = key.getAppsecret();
            String approvalKey = approvalKeyService.getApprovalKey(appkey, appsecret);
            for (String trId : trIds) {
                List<String> stocksForThisSession = new ArrayList<>();
                for (int i = 0; i < maxPerSession && idx < allStocks.size(); i++, idx++) {
                    stocksForThisSession.add(allStocks.get(idx));
                }
                if (!stocksForThisSession.isEmpty()) {
                    try {
                        URI uri = new URI("ws://ops.koreainvestment.com:21000");
                        StockWebSocketClient client = new StockWebSocketClient(uri, appkey, approvalKey, trId, stocksForThisSession);
                        client.connect();
                        clients.add(client);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}