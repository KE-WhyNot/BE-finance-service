package com.youthfi.finance.domain.stock.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name = "dividend_infos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DividendInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dividend_id")
    private Long dividendId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate; // 배당기준일

    @Column(name = "cash_dividend", nullable = false, precision = 15, scale = 2)
    private BigDecimal cashDividend; // 현금배당금

    @Column(name = "dividend_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal dividendRate; // 현금배당률

    @Column(name = "dividend_date", nullable = false)
    private LocalDate dividendDate; // 배당금지급일

    @Builder
    public DividendInfo(Stock stock, Sector sector, LocalDate recordDate, 
                       BigDecimal cashDividend, BigDecimal dividendRate, LocalDate dividendDate) {
        this.stock = stock;
        this.sector = sector;
        this.recordDate = recordDate;
        this.cashDividend = cashDividend;
        this.dividendRate = dividendRate;
        this.dividendDate = dividendDate;
    }

    public void updateDividendInfo(LocalDate recordDate, BigDecimal cashDividend, 
                                 BigDecimal dividendRate, LocalDate dividendDate) {
        this.recordDate = recordDate;
        this.cashDividend = cashDividend;
        this.dividendRate = dividendRate;
        this.dividendDate = dividendDate;
    }

    public boolean isDividendPaid() {
        return LocalDate.now().isAfter(this.dividendDate);
    }

    public boolean isRecordDatePassed() {
        return LocalDate.now().isAfter(this.recordDate);
    }
}
