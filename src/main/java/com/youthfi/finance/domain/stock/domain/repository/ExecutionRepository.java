package com.youthfi.finance.domain.stock.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.stock.domain.entity.Execution;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {
    
    // 사용자별 거래내역 조회
    List<Execution> findByUserUserIdOrderByExecutedAtDesc(String userId);
    
    // 특정 종목 거래내역
    List<Execution> findByUserUserIdAndStockStockIdOrderByExecutedAtDesc(String userId, String stockId);
    
    // 매수/매도별 거래내역
    List<Execution> findByUserUserIdAndExecutionTypeOrderByExecutedAtDesc(String userId, Execution.ExecutionType executionType);
    
    // 특정 섹터 거래내역
    List<Execution> findByUserUserIdAndSectorSectorIdOrderByExecutedAtDesc(String userId, Long sectorId);
    
    // 특정 날짜의 거래내역
    List<Execution> findByUserUserIdAndExecutedAtOrderByExecutedAtDesc(String userId, LocalDateTime executedAt);
}
