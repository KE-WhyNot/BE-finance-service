package com.youthfi.finance.domain.stock.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.global.exception.StockException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "userstock")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userStockId")
    private Long userStockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockId", nullable = false)
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sectorId", nullable = false)
    private Sector sector;

    @Column(name = "holdingQuantity", nullable = false)
    private Long holdingQuantity; // 보유수량

    @Column(name = "avgPrice", nullable = false, precision = 15, scale = 2)
    private BigDecimal avgPrice; // 평균 매입가

    @Column(name = "totalValue", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalValue; // 총 평가금액

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp @Column(name = "updatedAt", columnDefinition = "TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;


    @Builder
    public UserStock(User user, Stock stock, Sector sector, Long holdingQuantity, 
                    BigDecimal avgPrice, BigDecimal totalValue) {
        this.user = user;
        this.stock = stock;
        this.sector = sector;
        this.holdingQuantity = holdingQuantity;
        this.avgPrice = avgPrice;
        this.totalValue = totalValue;
    }

    public void updateHoldingQuantity(Long holdingQuantity) {
        this.holdingQuantity = holdingQuantity;
    }

    public void updateAvgPrice(BigDecimal avgPrice) {
        this.avgPrice = avgPrice;
    }

    public void updateTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public void addQuantity(Long quantity, BigDecimal price) {
        // 평균 매입가 계산
        BigDecimal currentTotal = this.avgPrice.multiply(BigDecimal.valueOf(this.holdingQuantity)); // 현재까지 보유 주식 총 금액
        BigDecimal newTotal = price.multiply(BigDecimal.valueOf(quantity)); // 새로 매입한 주식 총 금액
        BigDecimal totalQuantity = BigDecimal.valueOf(this.holdingQuantity + quantity); // 총 보유 주식 수량
        
        this.holdingQuantity += quantity; // 보유 주식 수량 증가
        this.avgPrice = currentTotal.add(newTotal).divide(totalQuantity, 2, java.math.RoundingMode.HALF_UP); // 평균 매입가 계산
        this.totalValue = this.avgPrice.multiply(BigDecimal.valueOf(this.holdingQuantity)); // 총 평가금액 계산
    }

    public void subtractQuantity(Long quantity) {
        StockException.validateQuantity(quantity);
        if (quantity > this.holdingQuantity) {
            throw StockException.insufficientStockQuantity(this.stock.getStockId(), quantity, this.holdingQuantity);
        }
        this.holdingQuantity -= quantity;
        this.totalValue = this.avgPrice.multiply(BigDecimal.valueOf(this.holdingQuantity));
    }
    
}
