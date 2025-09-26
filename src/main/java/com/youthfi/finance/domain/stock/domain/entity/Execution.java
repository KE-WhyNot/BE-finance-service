package com.youthfi.finance.domain.stock.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name = "executions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Execution extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "execution_id")
    private Long executionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @Column(name = "date", nullable = false)
    private LocalDate date; // 거래일

    @Column(name = "is_buy", nullable = false)
    private Boolean isBuy; // 매수여부

    @Column(name = "quantity", nullable = false)
    private Long quantity; // 거래수량

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price; // 거래단가

    @Column(name = "total_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalPrice; // 거래총액

    @Builder
    public Execution(User user, Stock stock, Sector sector, LocalDate date, 
                   Boolean isBuy, Long quantity, BigDecimal price, BigDecimal totalPrice) {
        this.user = user;
        this.stock = stock;
        this.sector = sector;
        this.date = date;
        this.isBuy = isBuy;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public void updateExecution(LocalDate date, Boolean isBuy, Long quantity, 
                              BigDecimal price, BigDecimal totalPrice) {
        this.date = date;
        this.isBuy = isBuy;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public boolean isBuyOrder() {
        return this.isBuy;
    }

    public boolean isSellOrder() {
        return !this.isBuy;
    }
}
