package com.youthfi.finance.domain.stock.domain.entity;

import java.math.BigDecimal;

import com.youthfi.finance.domain.user.domain.entity.Portfolio;
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
@Table(name = "portfolio_stocks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioStock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_stock_id")
    private Long portfolioStockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @Column(name = "allocation_pct", nullable = false, precision = 5, scale = 2)
    private BigDecimal allocationPct; // 포트폴리오 내 비율(%)

    @Builder
    public PortfolioStock(Portfolio portfolio, Stock stock, Sector sector, BigDecimal allocationPct) {
        this.portfolio = portfolio;
        this.stock = stock;
        this.sector = sector;
        this.allocationPct = allocationPct;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public void updateAllocationPct(BigDecimal allocationPct) {
        this.allocationPct = allocationPct;
    }
}
