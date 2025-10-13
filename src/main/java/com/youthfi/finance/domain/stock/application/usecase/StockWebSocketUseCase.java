package com.youthfi.finance.domain.stock.application.usecase;

import com.youthfi.finance.domain.stock.domain.service.StockWebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StockWebSocketUseCase {

    private final StockWebSocketService stockWebSocketService;

    /**
     * 지정된 종목들에 대한 WebSocket 시작 
     */
    @Transactional
    public void startWebSocketsForAllKeysAndStocks(List<String> stockCodes) {
        log.info("WebSocket 시작 요청 - 종목수: {}", stockCodes.size());
        
        stockWebSocketService.startWebSocketsForAllKeysAndStocks(stockCodes);
        
        log.info("WebSocket 시작 완료 - 종목수: {}", stockCodes.size());
    }

    /**
     * 모든 WebSocket 연결 종료 
     */
    @Transactional
    public void stopAllWebSockets() {
        log.info("WebSocket 종료 요청");
        
        stockWebSocketService.stopAllWebSockets();
        
        log.info("WebSocket 종료 완료");
    }

    /**
     * WebSocket 연결 상태 확인 
     */
    public int getActiveConnectionCount() {
        log.info("WebSocket 연결 상태 확인 요청");
        
        int count = stockWebSocketService.getActiveConnectionCount();
        
        log.info("WebSocket 연결 상태 확인 완료 - 활성 연결수: {}", count);
        return count;
    }
}
