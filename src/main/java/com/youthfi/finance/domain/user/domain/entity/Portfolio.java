package com.youthfi.finance.domain.user.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.youthfi.finance.domain.stock.domain.entity.PortfolioStock;
import com.youthfi.finance.global.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "portfolios")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private Long portfolioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "portfolio_name", nullable = false, length = 100)
    private String portfolioName;

    @Column(name = "allocation_stocks", nullable = false, precision = 5, scale = 2)
    private BigDecimal allocationStocks;

    @Column(name = "allocation_savings", nullable = false, precision = 5, scale = 2)
    private BigDecimal allocationSavings;

    @Column(name = "expected_1yr_return", nullable = false, precision = 5, scale = 2)
    private BigDecimal expected1YrReturn;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioStock> portfolioStocks = new ArrayList<>();

    @Builder
    public Portfolio(User user, String portfolioName, BigDecimal allocationStocks, 
                   BigDecimal allocationSavings, BigDecimal expected1YrReturn) {
        this.user = user;
        this.portfolioName = portfolioName;
        this.allocationStocks = allocationStocks;
        this.allocationSavings = allocationSavings;
        this.expected1YrReturn = expected1YrReturn;
    }

    public void updatePortfolio(String portfolioName, BigDecimal allocationStocks, 
                              BigDecimal allocationSavings, BigDecimal expected1YrReturn) {
        this.portfolioName = portfolioName;
        this.allocationStocks = allocationStocks;
        this.allocationSavings = allocationSavings;
        this.expected1YrReturn = expected1YrReturn;
    }

    public void addStock(PortfolioStock portfolioStock) {
        this.portfolioStocks.add(portfolioStock);
        portfolioStock.setPortfolio(this);
    }

    public void removeStock(PortfolioStock portfolioStock) {
        this.portfolioStocks.remove(portfolioStock);
        portfolioStock.setPortfolio(null);
    }
}
