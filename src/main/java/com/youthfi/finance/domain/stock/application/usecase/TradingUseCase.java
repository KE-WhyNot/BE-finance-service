package com.youthfi.finance.domain.stock.application.usecase;

import com.youthfi.finance.domain.stock.application.dto.request.BuyStockRequest;
import com.youthfi.finance.domain.stock.application.dto.request.SellStockRequest;
import com.youthfi.finance.domain.stock.application.dto.response.ExecutionResponse;
import com.youthfi.finance.domain.stock.application.mapper.StockMapper;
import com.youthfi.finance.domain.stock.domain.entity.Execution;
import com.youthfi.finance.domain.stock.domain.service.TradingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
     * 주식 매수 
     */
    @Transactional
    public ExecutionResponse buyStock(String userId, BuyStockRequest request) {
        log.info("주식 매수 요청 - 사용자: {}, 종목: {}, 수량: {}, 가격: {}",
                userId, request.getStockId(), request.getQuantity(), request.getPrice());
        
        // 1. 도메인 서비스를 통한 매수 실행 (비즈니스 규칙 검증 포함)
        Execution execution = tradingService.executeBuy(userId, request.getStockId(), 
                request.getQuantity().longValue(), request.getPrice());
        
        // 2. 응답 DTO 변환
        ExecutionResponse response = stockMapper.toExecutionResponse(execution);
        
        log.info("주식 매수 완료 - 거래ID: {}", execution.getExecutionId());
        return response;
    }

    /**
     * 주식 매도 
     */
    @Transactional
    public ExecutionResponse sellStock(String userId, SellStockRequest request) {
        log.info("주식 매도 요청 - 사용자: {}, 종목: {}, 수량: {}, 가격: {}", 
                userId, request.getStockId(), request.getQuantity(), request.getPrice());
        
        // 1. 도메인 서비스를 통한 매도 실행 (비즈니스 규칙 검증 포함)
        Execution execution = tradingService.executeSell(userId, request.getStockId(), 
                request.getQuantity().longValue(), request.getPrice());
        
        // 2. 응답 DTO 변환
        ExecutionResponse response = stockMapper.toExecutionResponse(execution);
        
        log.info("주식 매도 완료 - 거래ID: {}", execution.getExecutionId());
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
