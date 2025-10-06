package com.youthfi.finance.domain.portfolio.domain.service;

import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioStockService {

    private final PortfolioStockRepository portfolioStockRepository;
    private final PortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;
    private final SectorRepository sectorRepository;

    /**
     * 포트폴리오에 새로운 종목을 추가합니다.
     * 포트폴리오와 종목 존재 여부 확인, 배분 비율 유효성 검사를 수행합니다.
     */
    
    public PortfolioStock addStockToPortfolio(Long portfolioId, String stockId, BigDecimal allocationPct) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("포트폴리오를 찾을 수 없습니다: " + portfolioId));

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("종목을 찾을 수 없습니다: " + stockId));

        if (allocationPct.compareTo(BigDecimal.ZERO) <= 0 || allocationPct.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("배분 비율은 0% 초과 100% 이하여야 합니다.");
        }

        PortfolioStock portfolioStock = PortfolioStock.builder()
                .portfolio(portfolio)
                .stock(stock)
                .allocationPct(allocationPct)
                .build();

        return portfolioStockRepository.save(portfolioStock);
    }

    /**
     * 특정 포트폴리오에 포함된 모든 종목 리스트를 조회합니다.
     */

    public List<PortfolioStock> getStocksByPortfolioId(Long portfolioId) {
        return portfolioStockRepository.findByPortfolioPortfolioId(portfolioId);
    }

    /**
     * 특정 포트폴리오에 속한 모든 종목의 배분 비율 합계를 반환합니다.
     */

    public java.math.BigDecimal getTotalAllocationPct(Long portfolioId) {
        List<PortfolioStock> stocks = getStocksByPortfolioId(portfolioId);
        return stocks.stream()
                .map(PortfolioStock::getAllocationPct)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }

    /**
     * 특정 포트폴리오에 포함된 종목 개수를 반환합니다.
     */

    public long getStockCountByPortfolioId(Long portfolioId) {
        return portfolioStockRepository.findByPortfolioPortfolioId(portfolioId).size();
    }
}


