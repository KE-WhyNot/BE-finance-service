package com.youthfi.finance.domain.stock.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.youthfi.finance.domain.user.domain.entity.UserStock;
import com.youthfi.finance.global.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "stocks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {

    @Id
    @Column(name = "stock_id", length = 10)
    private String stockId; // 종목코드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @Column(name = "stock_name", nullable = false, length = 100)
    private String stockName;

    @Column(name = "total_stock", precision = 20, scale = 0)
    private BigDecimal totalStock; // 시가총액

    @Column(name = "stock_outline", columnDefinition = "TEXT")
    private String stockOutline;

    @Column(name = "stock_image", length = 500)
    private String stockImage;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioStock> portfolioStocks = new ArrayList<>();

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStock> userStocks = new ArrayList<>();

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Execution> executions = new ArrayList<>();

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterestStock> interestStocks = new ArrayList<>();

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DividendInfo> dividendInfos = new ArrayList<>();

    @Builder
    public Stock(String stockId, Sector sector, String stockName, BigDecimal totalStock, 
                String stockOutline, String stockImage) {
        this.stockId = stockId;
        this.sector = sector;
        this.stockName = stockName;
        this.totalStock = totalStock;
        this.stockOutline = stockOutline;
        this.stockImage = stockImage;
    }

    public void updateStock(String stockName, BigDecimal totalStock, 
                          String stockOutline, String stockImage) {
        this.stockName = stockName;
        this.totalStock = totalStock;
        this.stockOutline = stockOutline;
        this.stockImage = stockImage;
    }

    public void updateSector(Sector sector) {
        this.sector = sector;
    }
}
