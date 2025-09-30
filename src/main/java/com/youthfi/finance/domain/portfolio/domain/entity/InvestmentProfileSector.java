package com.youthfi.finance.domain.portfolio.domain.entity;

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
@Table(name = "investment_profile_sectors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InvestmentProfileSector extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private InvestmentProfile investmentProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @Builder
    public InvestmentProfileSector(InvestmentProfile investmentProfile, Sector sector) {
        this.investmentProfile = investmentProfile;
        this.sector = sector;
    }
}


