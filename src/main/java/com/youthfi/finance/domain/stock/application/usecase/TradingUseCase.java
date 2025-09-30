package com.youthfi.finance.domain.stock.application.usecase;

import com.youthfi.finance.domain.stock.domain.entity.Execution;
import com.youthfi.finance.domain.stock.domain.service.TradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TradingUseCase {

    private final TradingService tradingService;

    /**
     * 주식 매수
     */
    @Transactional
    public Execution buyStock(Long userId, String stockId, Integer quantity, BigDecimal price) {
        // 수량 검증
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }
        
        // 가격 검증
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("가격은 0보다 커야 합니다.");
        }

        return tradingService.executeBuy(userId, stockId, quantity.longValue(), price);
    }

    /**
     * 주식 매도
     */
    @Transactional
    public Execution sellStock(Long userId, String stockId, Integer quantity, BigDecimal price) {
        // 수량 검증
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }
        
        // 가격 검증
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("가격은 0보다 커야 합니다.");
        }

        return tradingService.executeSell(userId, stockId, quantity.longValue(), price);
    }

    /**
     * 내 거래 내역 조회
     */
    public List<Execution> getMyTradingHistory(Long userId) {
        return tradingService.getExecutionsByUserId(userId);
    }

    /**
     * 특정 종목 거래 내역 조회
     */
    public List<Execution> getMyTradingHistoryByStock(Long userId, String stockId) {
        return tradingService.getExecutionsByUserIdAndStockId(userId, stockId);
    }

    /**
     * 매수 내역 조회
     */
    public List<Execution> getMyBuyHistory(Long userId) {
        return tradingService.getExecutionsByUserIdAndIsBuy(userId, true);
    }

    /**
     * 매도 내역 조회
     */
    public List<Execution> getMySellHistory(Long userId) {
        return tradingService.getExecutionsByUserIdAndIsBuy(userId, false);
    }

    /**
     * 특정 날짜 거래 내역 조회
     */
    public List<Execution> getMyTradingHistoryByDate(Long userId, LocalDate date) {
        return tradingService.getExecutionsByUserIdAndDate(userId, date);
    }

    /**
     * 내 총 거래 금액 조회
     */
    public BigDecimal getMyTotalTradingAmount(Long userId) {
        return tradingService.getTotalTradingAmount(userId);
    }

    /**
     * 내 매수 총액 조회
     */
    public BigDecimal getMyTotalBuyAmount(Long userId) {
        return tradingService.getTotalBuyAmount(userId);
    }

    /**
     * 내 매도 총액 조회
     */
    public BigDecimal getMyTotalSellAmount(Long userId) {
        return tradingService.getTotalSellAmount(userId);
    }

    /**
     * 내 거래 수익률 계산
     */
    public BigDecimal calculateMyTradingReturnRate(Long userId) {
        BigDecimal totalBuy = getMyTotalBuyAmount(userId);
        BigDecimal totalSell = getMyTotalSellAmount(userId);
        
        if (totalBuy.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        // 거래 수익률 = (매도 총액 - 매수 총액) / 매수 총액 * 100
        BigDecimal profit = totalSell.subtract(totalBuy);
        return profit.divide(totalBuy, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * 내 거래 수익금 계산
     */
    public BigDecimal calculateMyTradingProfit(Long userId) {
        BigDecimal totalBuy = getMyTotalBuyAmount(userId);
        BigDecimal totalSell = getMyTotalSellAmount(userId);
        
        return totalSell.subtract(totalBuy);
    }
}
