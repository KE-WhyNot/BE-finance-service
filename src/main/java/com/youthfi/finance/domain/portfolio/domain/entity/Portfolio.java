package com.youthfi.finance.domain.portfolio.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.youthfi.finance.domain.user.domain.entity.User;
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
    @Column(name = "portfolioId")
    private Long portfolioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "portfolioName", nullable = false, length = 100)
    private String portfolioName;

    @Column(name = "highestValue", precision = 18, scale = 2)
    private BigDecimal highestValue;

    @Column(name = "lowestValue", precision = 18, scale = 2)
    private BigDecimal lowestValue;

    @Column(name = "allocation_savings", precision = 5, scale = 2)
    private BigDecimal allocationSavings;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioStock> portfolioStocks = new ArrayList<>();

    @Builder
    public Portfolio(User user, String portfolioName, BigDecimal highestValue, BigDecimal lowestValue, BigDecimal allocationSavings) {
        this.user = user;
        this.portfolioName = portfolioName;
        this.highestValue = highestValue;
        this.lowestValue = lowestValue;
        this.allocationSavings = allocationSavings;
    }

    public void updatePortfolio(String portfolioName, BigDecimal highestValue, BigDecimal lowestValue, BigDecimal allocationSavings) {
        this.portfolioName = portfolioName;
        this.highestValue = highestValue;
        this.lowestValue = lowestValue;
        this.allocationSavings = allocationSavings;
    }

    // Lombok @Getter가 작동하지 않는 경우를 대비한 명시적 getter 메서드들
    public Long getPortfolioId() {
        return portfolioId;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public BigDecimal getHighestValue() {
        return highestValue;
    }

    public BigDecimal getLowestValue() {
        return lowestValue;
    }

}


