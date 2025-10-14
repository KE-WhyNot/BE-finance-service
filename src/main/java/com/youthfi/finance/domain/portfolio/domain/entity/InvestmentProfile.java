package com.youthfi.finance.domain.portfolio.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.global.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "investment_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InvestmentProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "investment_profile", nullable = false, length = 20)
    private InvestmentProfileType investmentProfile;

    @Column(name = "available_assets", nullable = false, precision = 15, scale = 2)
    private BigDecimal availableAssets;

    @Enumerated(EnumType.STRING)
    @Column(name = "investment_goal", nullable = false, length = 20)
    private InvestmentGoal investmentGoal;

    @Enumerated(EnumType.STRING)
    @Column(name = "loss_tolerance", nullable = false, length = 20)
    private LossTolerance lossTolerance;

    @Enumerated(EnumType.STRING)
    @Column(name = "financial_knowledge", nullable = false, length = 20)
    private FinancialKnowledge financialKnowledge;

    @Enumerated(EnumType.STRING)
    @Column(name = "expected_profit", nullable = false, length = 20)
    private ExpectedProfit expectedProfit;

    @OneToMany(mappedBy = "investmentProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentProfileSector> investmentProfileSectors = new ArrayList<>();

    @Builder
    public InvestmentProfile(User user, InvestmentProfileType investmentProfile,
                           BigDecimal availableAssets, InvestmentGoal investmentGoal,
                           LossTolerance lossTolerance, FinancialKnowledge financialKnowledge,
                           ExpectedProfit expectedProfit) {
        this.user = user;
        this.investmentProfile = investmentProfile;
        this.availableAssets = availableAssets;
        this.investmentGoal = investmentGoal;
        this.lossTolerance = lossTolerance;
        this.financialKnowledge = financialKnowledge;
        this.expectedProfit = expectedProfit;
    }

    // record 기반 DTO 사용 호환을 위한 접근자 명시
    public Long getProfileId() {
        return profileId;
    }

    public User getUser() {
        return user;
    }

    public InvestmentProfileType getInvestmentProfile() {
        return investmentProfile;
    }

    public BigDecimal getAvailableAssets() {
        return availableAssets;
    }

    public InvestmentGoal getInvestmentGoal() {
        return investmentGoal;
    }

    public LossTolerance getLossTolerance() {
        return lossTolerance;
    }

    public FinancialKnowledge getFinancialKnowledge() {
        return financialKnowledge;
    }

    public ExpectedProfit getExpectedProfit() {
        return expectedProfit;
    }

    public List<InvestmentProfileSector> getInvestmentProfileSectors() {
        return investmentProfileSectors;
    }

    public void updateProfile(InvestmentProfileType investmentProfile,
                            BigDecimal availableAssets, InvestmentGoal investmentGoal,
                            LossTolerance lossTolerance, FinancialKnowledge financialKnowledge,
                            ExpectedProfit expectedProfit) {
        this.investmentProfile = investmentProfile;
        this.availableAssets = availableAssets;
        this.investmentGoal = investmentGoal;
        this.lossTolerance = lossTolerance;
        this.financialKnowledge = financialKnowledge;
        this.expectedProfit = expectedProfit;
    }

    public enum InvestmentProfileType {
        CONSERVATIVE("안정형"),
        CONSERVATIVE_SEEKING("안정추구형"),
        RISK_NEUTRAL("위험중립형"),
        AGGRESSIVE("적극투자형"),
        VERY_AGGRESSIVE("공격투자형");

        private final String description;

        InvestmentProfileType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum InvestmentGoal {
        EDUCATION("학비"),
        LIVING_EXPENSES("생활비"),
        HOUSE_PURCHASE("주택마련"),
        ASSET_GROWTH("자산증식"),
        DEBT_REPAYMENT("채무상환");

        private final String description;

        InvestmentGoal(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum LossTolerance {
        NO_LOSS("원금 손실 없음"),
        TEN_PERCENT("원금의 10%"),
        THIRTY_PERCENT("원금의 30%"),
        FIFTY_PERCENT("원금의 50%"),
        SEVENTY_PERCENT("원금의 70%"),
        FULL_AMOUNT("원금 전액");

        private final String description;

        LossTolerance(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum FinancialKnowledge {
        VERY_LOW("매우 낮음"),
        LOW("낮음"),
        MEDIUM("보통"),
        HIGH("높음"),
        VERY_HIGH("매우 높음");

        private final String description;

        FinancialKnowledge(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum ExpectedProfit {
        ONE_FIFTY_PERCENT("150%"),
        TWO_HUNDRED_PERCENT("200%"),
        TWO_FIFTY_PERCENT("250%"),
        THREE_HUNDRED_PERCENT_PLUS("300% 이상");

        private final String description;

        ExpectedProfit(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum AvailableAssets {
        THOUSAND("천원"),
        TEN_THOUSAND("만원"),
        HUNDRED_THOUSAND("십만원"),
        MILLION("백만원"),
        TEN_MILLION("천만원");

        private final String description;

        AvailableAssets(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}


