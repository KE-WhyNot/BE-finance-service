package com.youthfi.finance.domain.stock.domain.service;

import com.youthfi.finance.domain.stock.domain.entity.Execution;
import com.youthfi.finance.domain.stock.domain.entity.Stock;
import com.youthfi.finance.domain.stock.domain.entity.Sector;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.stock.domain.entity.UserStock;
import com.youthfi.finance.domain.stock.domain.repository.UserStockRepository;
import com.youthfi.finance.domain.stock.domain.repository.ExecutionRepository;
import com.youthfi.finance.domain.stock.domain.repository.StockRepository;
import com.youthfi.finance.domain.stock.domain.repository.SectorRepository;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import com.youthfi.finance.global.exception.StockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final UserStockRepository userStockRepository;

    /**
     * 주식 매수 처리 
     */
    @Transactional
    public Execution executeBuy(String userId, String stockId, Long quantity, BigDecimal price) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> StockException.userNotFound(userId));
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> StockException.stockNotFound(stockId));

        // 비즈니스 규칙 검증
        validateTradingRequest(quantity, price);
        validateUserBalance(user, price, quantity);
        
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));
        user.subtractBalance(totalPrice);

        // 거래 내역 저장
        Execution execution = Execution.builder()
                .user(user)
                .stock(stock)
                .sector(stock.getSector())
                .executedAt(LocalDateTime.now())
                .executionType(Execution.ExecutionType.BUY)
                .quantity(quantity)
                .price(price)
                .totalPrice(totalPrice)
                .build();
        Execution saved = executionRepository.save(execution);

        // 보유주식 업데이트 (평균 매입가/보유수량/평가금액)
        UserStock userStock = userStockRepository.findByUserUserIdAndStockStockId(userId, stockId)
                .orElseGet(() -> userStockRepository.save(
                        UserStock.builder()
                                .user(user)
                                .stock(stock)
                                .sector(stock.getSector())
                                .holdingQuantity(0L)
                                .avgPrice(BigDecimal.ZERO)
                                .totalValue(BigDecimal.ZERO)
                                .build()
                ));
        userStock.addQuantity(quantity, price);
        userStockRepository.save(userStock);

        return saved;
    }

    /**
     * 주식 매도 처리
     */
    @Transactional
    public Execution executeSell(String userId, String stockId, Long quantity, BigDecimal price) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> StockException.userNotFound(userId));
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> StockException.stockNotFound(stockId));

        // 비즈니스 규칙 검증
        validateTradingRequest(quantity, price);
        
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));
        user.addBalance(totalPrice);

        Execution execution = Execution.builder()
                .user(user)
                .stock(stock)
                .sector(stock.getSector())
                .executedAt(LocalDateTime.now())
                .executionType(Execution.ExecutionType.SELL)
                .quantity(quantity)
                .price(price)
                .totalPrice(totalPrice)
                .build();
        Execution saved = executionRepository.save(execution);

        // 보유주식 감소 처리
        UserStock userStock = userStockRepository.findByUserUserIdAndStockStockId(userId, stockId)
                .orElseThrow(() -> StockException.userStockNotFound());
        userStock.subtractQuantity(quantity);
        userStockRepository.save(userStock);

        return saved;
    }

    /**
     * 사용자별 거래 내역 조회 
     */
    public List<Execution> getExecutionsByUserId(String userId) {
        return executionRepository.findByUserUserIdOrderByExecutedAtDesc(userId);
    }

    /**
     * 특정 종목 거래 내역 조회 
     */
    public List<Execution> getExecutionsByUserIdAndStockId(String userId, String stockId) {
        return executionRepository.findByUserUserIdAndStockStockIdOrderByExecutedAtDesc(userId, stockId);
    }

    /**
     * 매수/매도별 거래 내역 조회 
     */
    public List<Execution> getExecutionsByUserIdAndIsBuy(String userId, Boolean isBuy) {
        return executionRepository.findByUserUserIdAndExecutionTypeOrderByExecutedAtDesc(userId, 
                isBuy ? Execution.ExecutionType.BUY : Execution.ExecutionType.SELL);
    }

    /**
     * 거래 내역 조회 (ID)
     */
    public Optional<Execution> getExecutionById(Long executionId) {
        return executionRepository.findById(executionId);
    }


    // ==================== 비즈니스 규칙 검증 ====================

    /**
     * 거래 요청 검증
     */
    private void validateTradingRequest(Long quantity, BigDecimal price) {
        StockException.validateQuantity(quantity);
        StockException.validatePrice(price.longValue());
    }

    /**
     * 사용자 잔고 검증
     */
    private void validateUserBalance(User user, BigDecimal price, Long quantity) {
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));
        StockException.validateSufficientBalance(user.getBalance(), totalPrice);
    }
}
