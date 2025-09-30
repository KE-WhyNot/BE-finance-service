package com.youthfi.finance.domain.portfolio.domain.service;

import com.youthfi.finance.domain.portfolio.domain.entity.PortfolioStock;
import com.youthfi.finance.domain.stock.domain.entity.Stock;
import com.youthfi.finance.domain.portfolio.domain.repository.PortfolioStockRepository;
import com.youthfi.finance.domain.stock.domain.repository.StockRepository;
import com.youthfi.finance.domain.stock.domain.repository.SectorRepository;
import com.youthfi.finance.domain.portfolio.domain.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioStockService {

    private final PortfolioStockRepository portfolioStockRepository;
    private final PortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;
    private final SectorRepository sectorRepository;

    @Transactional
    public PortfolioStock addStockToPortfolio(Long portfolioId, String stockId, BigDecimal allocationPct) {
        var portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("포트폴리오를 찾을 수 없습니다: " + portfolioId));

        var stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("종목을 찾을 수 없습니다: " + stockId));

        if (allocationPct.compareTo(BigDecimal.ZERO) <= 0 || allocationPct.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("배분 비율은 0% 초과 100% 이하여야 합니다.");
        }

        PortfolioStock portfolioStock = PortfolioStock.builder()
                .portfolio(portfolio)
                .stock(stock)
                .sector(stock.getSector())
                .allocationPct(allocationPct)
                .build();

        return portfolioStockRepository.save(portfolioStock);
    }

    public List<PortfolioStock> getStocksByPortfolioId(Long portfolioId) {
        return portfolioStockRepository.findByPortfolioPortfolioId(portfolioId);
    }

    public List<PortfolioStock> getPortfoliosByStockId(String stockId) {
        return portfolioStockRepository.findByStockStockId(stockId);
    }

    public List<PortfolioStock> getPortfolioStocksBySectorId(Long sectorId) {
        return portfolioStockRepository.findBySectorSectorId(sectorId);
    }

    public Optional<PortfolioStock> getPortfolioStock(Long portfolioId, String stockId) {
        return portfolioStockRepository.findByPortfolioPortfolioIdAndStockStockId(portfolioId, stockId);
    }

    @Transactional
    public PortfolioStock updateAllocationPct(Long portfolioId, String stockId, BigDecimal newAllocationPct) {
        PortfolioStock portfolioStock = getPortfolioStock(portfolioId, stockId)
                .orElseThrow(() -> new RuntimeException("포트폴리오 종목을 찾을 수 없습니다."));

        if (newAllocationPct.compareTo(BigDecimal.ZERO) <= 0 || newAllocationPct.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("배분 비율은 0% 초과 100% 이하여야 합니다.");
        }

        portfolioStock.updateAllocationPct(newAllocationPct);
        return portfolioStockRepository.save(portfolioStock);
    }

    @Transactional
    public void removeStockFromPortfolio(Long portfolioId, String stockId) {
        PortfolioStock portfolioStock = getPortfolioStock(portfolioId, stockId)
                .orElseThrow(() -> new RuntimeException("포트폴리오 종목을 찾을 수 없습니다."));

        portfolioStockRepository.delete(portfolioStock);
    }

    public java.math.BigDecimal getTotalAllocationPct(Long portfolioId) {
        List<PortfolioStock> stocks = getStocksByPortfolioId(portfolioId);
        return stocks.stream()
                .map(PortfolioStock::getAllocationPct)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }

    public long getStockCountByPortfolioId(Long portfolioId) {
        return portfolioStockRepository.findByPortfolioPortfolioId(portfolioId).size();
    }

    public boolean existsPortfolioStock(Long portfolioId, String stockId) {
        return getPortfolioStock(portfolioId, stockId).isPresent();
    }
}


