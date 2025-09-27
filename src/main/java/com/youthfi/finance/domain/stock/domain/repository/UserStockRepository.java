package com.youthfi.finance.domain.stock.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.user.domain.entity.UserStock;

@Repository
public interface UserStockRepository extends JpaRepository<UserStock, Long> {
    
    // 사용자별 보유주식 조회
    List<UserStock> findByUserUserId(Long userId);
    
    // 특정 종목 보유 여부
    Optional<UserStock> findByUserUserIdAndStockStockId(Long userId, String stockId);
    
    // 특정 섹터의 보유주식들
    List<UserStock> findByUserUserIdAndSectorSectorId(Long userId, Long sectorId);
}
