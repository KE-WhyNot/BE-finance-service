package com.youthfi.finance.domain.stock.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.youthfi.finance.domain.user.domain.entity.User;
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
@Table(name = "execution")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Execution extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "executionId")
    private Long executionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockId", nullable = false)
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sectorId", nullable = false)
    private Sector sector;

    @Column(name = "tradeAt", nullable = false)
    private LocalDateTime executedAt; // 거래일시

    @Column(name = "isBuy", nullable = false)
    private Integer isBuy; // 거래유형 (1: 매수, 0: 매도)

    @Column(name = "quantity", nullable = false)
    private Long quantity; // 거래수량

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price; // 거래단가

    @Column(name = "totalPrice", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalPrice; // 거래총액


    @Builder
    public Execution(User user, Stock stock, Sector sector, LocalDateTime executedAt, 
    Integer isBuy, Long quantity, BigDecimal price, BigDecimal totalPrice) {
        this.user = user;
        this.stock = stock;
        this.sector = sector;
        this.executedAt = executedAt;
        this.isBuy = isBuy;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
}
    // ==================== 편의 메서드 ====================
    
    public boolean isBuyOrder() {
        return isBuy != null && isBuy == 1;
    }
    
    public boolean isSellOrder() {
        return isBuy != null && isBuy == 0;
    }
}
