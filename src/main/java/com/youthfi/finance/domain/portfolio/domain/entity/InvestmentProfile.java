package com.youthfi.finance.domain.portfolio.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.youthfi.finance.domain.stock.domain.entity.Sector;
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

    @OneToMany(mappedBy = "investmentProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentProfileSector> investmentProfileSectors = new ArrayList<>();

    @Builder
    public InvestmentProfile(User user, InvestmentProfileType investmentProfile,
                           BigDecimal availableAssets, InvestmentGoal investmentGoal) {
        this.user = user;
        this.investmentProfile = investmentProfile;
        this.availableAssets = availableAssets;
        this.investmentGoal = investmentGoal;
    }

    // record 기반 DTO 사용 호환을 위한 접근자 명시
    public Long getProfileId() {
        return profileId;
    }

    public void updateProfile(InvestmentProfileType investmentProfile,
                            BigDecimal availableAssets, InvestmentGoal investmentGoal) {
        this.investmentProfile = investmentProfile;
        this.availableAssets = availableAssets;
        this.investmentGoal = investmentGoal;
    }

    public enum InvestmentProfileType {
        HIGH_RISK("고위험"),
        BALANCED("균형"),
        CONSERVATIVE("안정");

        private final String description;

        InvestmentProfileType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum InvestmentGoal {
        RETIREMENT("노후"),
        EDUCATION("교육"),
        HOUSING("주택"),
        EMERGENCY("비상금");

        private final String description;

        InvestmentGoal(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}


