package com.youthfi.finance.domain.stock.domain.entity;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interest_stocks", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "stock_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterestStock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockId", nullable = false)
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sectorId", nullable = false)
    private Sector sector;

    @Column(name = "interestFlag", nullable = false)
    private Boolean interestFlag; // 관심 여부

    @Builder
    public InterestStock(User user, Stock stock, Sector sector, Boolean interestFlag) {
        this.user = user;
        this.stock = stock;
        this.sector = sector;
        this.interestFlag = interestFlag;
    }

    public void toggleInterest() {
        this.interestFlag = !this.interestFlag;
    }

    public void setInterest(Boolean interestFlag) {
        this.interestFlag = interestFlag;
    }

    public boolean isInterested() {
        return this.interestFlag;
    }
}
