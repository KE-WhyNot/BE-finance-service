package com.youthfi.finance.domain.stock.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfileSector;
import com.youthfi.finance.global.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sectors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sector extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sectorId")
    private Long sectorId;

    @Column(name = "sectorName", nullable = false, length = 100)
    private String sectorName;

    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stock> stocks = new ArrayList<>();

    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentProfileSector> investmentProfileSectors = new ArrayList<>();

    @Builder
    public Sector(String sectorName) {
        this.sectorName = sectorName;
    }

    // Lombok @Getter가 작동하지 않는 경우를 대비한 명시적 getter 메서드들
    public Long getSectorId() {
        return sectorId;
    }

    public String getSectorName() {
        return sectorName;
    }
}
