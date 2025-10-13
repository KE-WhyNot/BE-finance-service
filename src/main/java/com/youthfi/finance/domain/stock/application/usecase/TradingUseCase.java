package com.youthfi.finance.domain.stock.application.usecase;

import com.youthfi.finance.domain.stock.application.dto.request.CurrentPriceTradingRequest;
import com.youthfi.finance.domain.stock.application.dto.response.ExecutionResponse;
import com.youthfi.finance.domain.stock.application.mapper.StockMapper;
import com.youthfi.finance.domain.stock.domain.entity.Execution;
import com.youthfi.finance.domain.stock.domain.service.TradingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TradingUseCase {

    private final TradingService tradingService;
    private final StockMapper stockMapper;

    /**
     * 주식 매수 (실시간 현재가로 자동 거래)
     */
    @Transactional
    public ExecutionResponse buyStockAtCurrentPrice(String userId, CurrentPriceTradingRequest request) {
        log.info("주식 매수 요청 - 사용자: {}, 종목: {}, 수량: {}, 시장: {}",
                userId, request.stockId(), request.quantity(), request.marketCode());
        
        // 1. 도메인 서비스를 통한 현재가 기반 매수 실행
        Execution execution = tradingService.executeBuyAtCurrentPrice(userId, request.stockId(), 
                request.quantity().longValue(), request.marketCode());
        
        // 2. 응답 DTO 변환
        ExecutionResponse response = stockMapper.toExecutionResponse(execution);
        
        log.info("주식 매수 완료 - 거래ID: {}, 체결가: {}", 
                execution.getExecutionId(), execution.getPrice());
        return response;
    }

    /**
     * 주식 매도 (실시간 현재가로 자동 거래)
     */
    @Transactional
    public ExecutionResponse sellStockAtCurrentPrice(String userId, CurrentPriceTradingRequest request) {
        log.info("주식 매도 요청 - 사용자: {}, 종목: {}, 수량: {}, 시장: {}",
                userId, request.stockId(), request.quantity(), request.marketCode());
        
        // 1. 도메인 서비스를 통한 현재가 기반 매도 실행
        Execution execution = tradingService.executeSellAtCurrentPrice(userId, request.stockId(), 
                request.quantity().longValue(), request.marketCode());
        
        // 2. 응답 DTO 변환
        ExecutionResponse response = stockMapper.toExecutionResponse(execution);
        
        log.info("주식 매도 완료 - 거래ID: {}, 체결가: {}", 
                execution.getExecutionId(), execution.getPrice());
        return response;
    }

    /**
     * 내 거래 내역 조회 
     */
    public List<ExecutionResponse> getMyTradingHistory(String userId) {
        log.info("거래 내역 조회 요청 - 사용자: {}", userId);
        
        List<Execution> executions = tradingService.getExecutionsByUserId(userId);
        List<ExecutionResponse> responses = executions.stream()
                .map(stockMapper::toExecutionResponse)
                .collect(Collectors.toList());
        
        log.info("거래 내역 조회 완료 - 사용자: {}, 건수: {}", userId, responses.size());
        return responses;
    }

    /**
     * 특정 종목 거래 내역 조회 
     */
    public List<ExecutionResponse> getMyTradingHistoryByStock(String userId, String stockId) {
        log.info("종목별 거래 내역 조회 요청 - 사용자: {}, 종목: {}", userId, stockId);
        
        List<Execution> executions = tradingService.getExecutionsByUserIdAndStockId(userId, stockId);
        List<ExecutionResponse> responses = executions.stream()
                .map(stockMapper::toExecutionResponse)
                .collect(Collectors.toList());
        
        log.info("종목별 거래 내역 조회 완료 - 사용자: {}, 종목: {}, 건수: {}", userId, stockId, responses.size());
        return responses;
    }

    /**
     * 매수 내역 조회 
     */
    public List<ExecutionResponse> getMyBuyHistory(String userId) {
        log.info("매수 내역 조회 요청 - 사용자: {}", userId);
        
        List<Execution> executions = tradingService.getExecutionsByUserIdAndIsBuy(userId, true);
        List<ExecutionResponse> responses = executions.stream()
                .map(stockMapper::toExecutionResponse)
                .collect(Collectors.toList());
        
        log.info("매수 내역 조회 완료 - 사용자: {}, 건수: {}", userId, responses.size());
        return responses;
    }

    /**
     * 매도 내역 조회
     */
    public List<ExecutionResponse> getMySellHistory(String userId) {
        log.info("매도 내역 조회 요청 - 사용자: {}", userId);
        
        List<Execution> executions = tradingService.getExecutionsByUserIdAndIsBuy(userId, false);
        List<ExecutionResponse> responses = executions.stream()
                .map(stockMapper::toExecutionResponse)
                .collect(Collectors.toList());
        
        log.info("매도 내역 조회 완료 - 사용자: {}, 건수: {}", userId, responses.size());
        return responses;
    }

}
