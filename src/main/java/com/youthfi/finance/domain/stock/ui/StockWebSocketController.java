package com.youthfi.finance.domain.stock.ui;

import com.youthfi.finance.domain.stock.application.usecase.StockRealtimeUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/stock/ws")
public class StockWebSocketController {
    private final StockRealtimeUseCase stockRealtimeUseCase;

    public StockWebSocketController(StockRealtimeUseCase stockRealtimeUseCase) {
        this.stockRealtimeUseCase = stockRealtimeUseCase;
    }

    @PostMapping("/start")
    public String startWebSocket() {
        List<String> stocks = List.of("005930", "000660", "035420"); // 예시: 삼성전자, SK하이닉스, NAVER
        stockRealtimeUseCase.startWebSocketsForAllKeysAndStocks(stocks);
        return "WebSocket 실시간 수신 시작";
    }
}