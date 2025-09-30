package com.youthfi.finance.domain.user.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.youthfi.finance.domain.stock.domain.entity.Execution;
import com.youthfi.finance.domain.stock.domain.entity.InterestStock;
import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.global.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "seed_money", nullable = false, precision = 15, scale = 2)
    private BigDecimal seedMoney;

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @Column(name = "profile_image", length = 500)
    private String profileImage;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private InvestmentProfile investmentProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolios = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStock> userStocks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Execution> executions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterestStock> interestStocks = new ArrayList<>();

    @Builder
    public User(String name, BigDecimal seedMoney, BigDecimal balance, String profileImage) {
        this.name = name;
        this.seedMoney = seedMoney;
        this.balance = balance;
        this.profileImage = profileImage;
    }

    public void updateProfile(String name, String profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }

    public void updateBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void addBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void subtractBalance(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }
        this.balance = this.balance.subtract(amount);
    }
}
