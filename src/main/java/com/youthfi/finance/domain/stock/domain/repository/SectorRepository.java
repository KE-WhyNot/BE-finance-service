package com.youthfi.finance.domain.stock.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.stock.domain.entity.Sector;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
    
    // 기본 조회
    Optional<Sector> findBySectorId(Long sectorId);
    
    // 섹터명으로 조회
    Optional<Sector> findBySectorName(String sectorName);
    
    // 섹터명으로 검색 (부분 일치)
    List<Sector> findBySectorNameContaining(String sectorName);
}
