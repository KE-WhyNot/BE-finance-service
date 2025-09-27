package com.youthfi.finance.domain.stock.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.stock.domain.entity.InterestStock;

@Repository
public interface InterestStockRepository extends JpaRepository<InterestStock, Long> {
    
    // 사용자별 관심종목 조회
    List<InterestStock> findByUserUserIdAndInterestFlagTrue(Long userId);
    
    // 특정 종목 관심 여부
    Optional<InterestStock> findByUserUserIdAndStockStockId(Long userId, String stockId);
    
    // 특정 섹터의 관심종목들
    List<InterestStock> findByUserUserIdAndSectorSectorIdAndInterestFlagTrue(Long userId, Long sectorId);
}
