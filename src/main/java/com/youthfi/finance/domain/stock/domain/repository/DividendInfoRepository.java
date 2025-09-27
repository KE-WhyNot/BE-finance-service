package com.youthfi.finance.domain.stock.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.stock.domain.entity.DividendInfo;

@Repository
public interface DividendInfoRepository extends JpaRepository<DividendInfo, Long> {
    
    // 종목별 배당정보 조회
    List<DividendInfo> findByStockStockId(String stockId);
    
    // 섹터별 배당정보 조회
    List<DividendInfo> findBySectorSectorId(Long sectorId);
    
    // 특정 종목의 특정 배당정보
    Optional<DividendInfo> findByStockStockIdAndRecordDate(String stockId, LocalDate recordDate);
    
    // 배당기준일로 조회
    List<DividendInfo> findByRecordDate(LocalDate recordDate);
    
    // 배당지급일로 조회
    List<DividendInfo> findByDividendDate(LocalDate dividendDate);
}
