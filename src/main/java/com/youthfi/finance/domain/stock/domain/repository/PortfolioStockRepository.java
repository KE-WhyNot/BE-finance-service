package com.youthfi.finance.domain.stock.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.stock.domain.entity.PortfolioStock;

@Repository
public interface PortfolioStockRepository extends JpaRepository<PortfolioStock, Long> {
    
    // 포트폴리오별 종목 조회
    List<PortfolioStock> findByPortfolioPortfolioId(Long portfolioId);
    
    // 특정 종목이 포함된 포트폴리오 종목들
    List<PortfolioStock> findByStockStockId(String stockId);
    
    // 특정 섹터의 포트폴리오 종목들
    List<PortfolioStock> findBySectorSectorId(Long sectorId);
    
    // 특정 포트폴리오의 특정 종목
    Optional<PortfolioStock> findByPortfolioPortfolioIdAndStockStockId(Long portfolioId, String stockId);
}
