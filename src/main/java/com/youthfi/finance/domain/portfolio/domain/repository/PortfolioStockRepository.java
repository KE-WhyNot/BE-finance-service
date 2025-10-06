package com.youthfi.finance.domain.portfolio.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.portfolio.domain.entity.PortfolioStock;

@Repository
public interface PortfolioStockRepository extends JpaRepository<PortfolioStock, Long> {
    List<PortfolioStock> findByPortfolioPortfolioId(Long portfolioId);
    Optional<PortfolioStock> findByPortfolioPortfolioIdAndStockStockId(Long portfolioId, String stockId);
}


