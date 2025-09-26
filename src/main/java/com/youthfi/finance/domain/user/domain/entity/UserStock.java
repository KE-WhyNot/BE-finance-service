package com.youthfi.finance.domain.user.domain.entity;

import java.math.BigDecimal;

import com.youthfi.finance.domain.stock.domain.entity.Stock;
import com.youthfi.finance.domain.stock.domain.entity.Sector;
import com.youthfi.finance.global.common.BaseEntity;

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

@Entity
@Table(name = "user_stocks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_stock_id")
    private Long userStockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @Column(name = "holding_quantity", nullable = false)
    private Long holdingQuantity; // 보유수량

    @Column(name = "avg_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal avgPrice; // 평균 매입가

    @Column(name = "total_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalValue; // 총 평가금액

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
        this.totalValue = this.avgPrice.multiply(BigDecimal.valueOf(this.holdingQuantity)); // 총 평가금액 계산  ->>> 근데 이걸 백엔드에 둘 필요가 있나 싶음. 프론트에서 계산하는 게 어떤지?
    }

    public void subtractQuantity(Long quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("감소할 수량은 0보다 커야 합니다.");
        }
        if (quantity > this.holdingQuantity) {
            throw new IllegalStateException("보유한 주식 수량이 부족합니다.");
        }
        this.holdingQuantity -= quantity;
        this.totalValue = this.avgPrice.multiply(BigDecimal.valueOf(this.holdingQuantity));
    }
    
}
