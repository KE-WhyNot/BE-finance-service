package com.youthfi.finance.domain.stock.domain.service;

import com.youthfi.finance.domain.stock.domain.entity.Execution;
import com.youthfi.finance.domain.stock.domain.entity.Stock;
import com.youthfi.finance.domain.stock.domain.entity.Sector;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.stock.domain.repository.ExecutionRepository;
import com.youthfi.finance.domain.stock.domain.repository.StockRepository;
import com.youthfi.finance.domain.stock.domain.repository.SectorRepository;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TradingService {

    private final ExecutionRepository executionRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final SectorRepository sectorRepository;

    /**
     * 주식 매수 처리
     */
    @Transactional
    public Execution executeBuy(Long userId, String stockId, Long quantity, BigDecimal price) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("종목을 찾을 수 없습니다: " + stockId));

        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));
        user.subtractBalance(totalPrice);

        // 거래 내역 저장
        Execution execution = Execution.builder()
                .user(user)
                .stock(stock)
                .sector(stock.getSector())
                .date(java.time.LocalDate.now())
                .isBuy(true)
                .quantity(quantity)
                .price(price)
                .totalPrice(totalPrice)
                .build();
        return executionRepository.save(execution);
    }

    /**
     * 주식 매도 처리
     */
    @Transactional
    public Execution executeSell(Long userId, String stockId, Long quantity, BigDecimal price) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("종목을 찾을 수 없습니다: " + stockId));

        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));
        user.addBalance(totalPrice);

        Execution execution = Execution.builder()
                .user(user)
                .stock(stock)
                .sector(stock.getSector())
                .date(java.time.LocalDate.now())
                .isBuy(false)
                .quantity(quantity)
                .price(price)
                .totalPrice(totalPrice)
                .build();
        return executionRepository.save(execution);
    }

    /**
     * 사용자별 거래 내역 조회
     */
    public List<Execution> getExecutionsByUserId(Long userId) {
        return executionRepository.findByUserUserIdOrderByDateDesc(userId);
    }

    /**
     * 특정 종목 거래 내역 조회
     */
    public List<Execution> getExecutionsByUserIdAndStockId(Long userId, String stockId) {
        return executionRepository.findByUserUserIdAndStockStockIdOrderByDateDesc(userId, stockId);
    }

    /**
     * 매수/매도별 거래 내역 조회
     */
    public List<Execution> getExecutionsByUserIdAndIsBuy(Long userId, Boolean isBuy) {
        return executionRepository.findByUserUserIdAndIsBuyOrderByDateDesc(userId, isBuy);
    }

    /**
     * 특정 날짜 거래 내역 조회
     */
    public List<Execution> getExecutionsByUserIdAndDate(Long userId, LocalDate date) {
        return executionRepository.findByUserUserIdAndDateOrderByCreatedAtDesc(userId, date);
    }

    /**
     * 거래 내역 조회 (ID)
     */
    public Optional<Execution> getExecutionById(Long executionId) {
        return executionRepository.findById(executionId);
    }

    /**
     * 거래 내역 삭제
     */
    @Transactional
    public void deleteExecution(Long executionId) {
        Execution execution = executionRepository.findById(executionId)
                .orElseThrow(() -> new RuntimeException("거래 내역을 찾을 수 없습니다: " + executionId));

        executionRepository.delete(execution);
    }

    /**
     * 사용자별 총 거래 금액 조회
     */
    public BigDecimal getTotalTradingAmount(Long userId) {
        List<Execution> executions = executionRepository.findByUserUserIdOrderByDateDesc(userId);
        return executions.stream()
                .map(Execution::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 사용자별 매수 총액 조회
     */
    public BigDecimal getTotalBuyAmount(Long userId) {
        List<Execution> buyExecutions = executionRepository.findByUserUserIdAndIsBuyOrderByDateDesc(userId, true);
        return buyExecutions.stream()
                .map(Execution::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 사용자별 매도 총액 조회
     */
    public BigDecimal getTotalSellAmount(Long userId) {
        List<Execution> sellExecutions = executionRepository.findByUserUserIdAndIsBuyOrderByDateDesc(userId, false);
        return sellExecutions.stream()
                .map(Execution::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
