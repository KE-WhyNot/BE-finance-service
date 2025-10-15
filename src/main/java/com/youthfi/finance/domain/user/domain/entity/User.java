package com.youthfi.finance.domain.user.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
import com.youthfi.finance.domain.stock.domain.entity.Execution;
import com.youthfi.finance.domain.stock.domain.entity.InterestStock;
import com.youthfi.finance.global.common.BaseEntity;
import com.youthfi.finance.global.exception.UserException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @Column(name = "userId", length = 50)
    private String userId;

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private InvestmentProfile investmentProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolios = new ArrayList<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Execution> executions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterestStock> interestStocks = new ArrayList<>();

    @Builder
    public User(String userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    /**
     * 잔고 차감 (매매 시)
     */
    public void subtractBalance(BigDecimal amount) {
        UserException.validateSufficientBalance(this.balance, amount);
        this.balance = this.balance.subtract(amount);
    }

    /**
     * 잔고 증가 (매매 시)
     */
    public void addBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
}
