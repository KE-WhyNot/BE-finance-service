package com.youthfi.finance.domain.stock.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.stock.domain.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {
    
    // 기본 조회
    Optional<Stock> findByStockId(String stockId);
    
    // 종목명으로 검색
    List<Stock> findByStockNameContaining(String stockName);
    
    // 섹터별 조회
    List<Stock> findBySectorSectorId(Long sectorId);
    List<Stock> findBySectorSectorName(String sectorName);
    
    // 전체 종목 조회
    List<Stock> findAllByOrderByStockId();
}
