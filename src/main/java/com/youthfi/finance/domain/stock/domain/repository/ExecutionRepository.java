package com.youthfi.finance.domain.stock.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.stock.domain.entity.Execution;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {
    
    // 사용자별 거래내역 조회
    List<Execution> findByUserUserIdOrderByDateDesc(Long userId);
    
    // 특정 종목 거래내역
    List<Execution> findByUserUserIdAndStockStockIdOrderByDateDesc(Long userId, String stockId);
    
    // 매수/매도별 거래내역
    List<Execution> findByUserUserIdAndIsBuyOrderByDateDesc(Long userId, Boolean isBuy);
    
    // 특정 섹터 거래내역
    List<Execution> findByUserUserIdAndSectorSectorIdOrderByDateDesc(Long userId, Long sectorId);
    
    // 특정 날짜의 거래내역
    List<Execution> findByUserUserIdAndDateOrderByCreatedAtDesc(Long userId, LocalDate date);
}
