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
     * DB에서 모든 종목 조회 
     */
    public List<String> getAllStockCodes() {
        log.info("DB에서 모든 종목 조회 요청");
        
        List<String> stockCodes = stockWebSocketService.getAllStockCodes();
        
        log.info("DB에서 모든 종목 조회 완료 - 종목수: {}", stockCodes.size());
        return stockCodes;
    }

    /**
     * 모든 키와 종목에 대한 WebSocket 시작 
     */
    @Transactional
    public void startWebSocketsForAllKeysAndStocks(List<String> allStocks) {
        log.info("WebSocket 시작 요청 - 종목수: {}", allStocks.size());
        
        stockWebSocketService.startWebSocketsForAllKeysAndStocks(allStocks);
        
        log.info("WebSocket 시작 완료 - 종목수: {}", allStocks.size());
    }

    /**
     * DB의 모든 종목으로 WebSocket 시작 
     */
    @Transactional
    public void startWebSocketsWithAllStocks() {
        log.info("DB의 모든 종목으로 WebSocket 시작 요청");
        
        // 1. DB에서 모든 종목 조회
        List<String> allStocks = getAllStockCodes();
        
        // 2. WebSocket 시작
        startWebSocketsForAllKeysAndStocks(allStocks);
        
        log.info("DB의 모든 종목으로 WebSocket 시작 완료 - 종목수: {}", allStocks.size());
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
