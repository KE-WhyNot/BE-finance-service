package com.youthfi.finance.domain.user.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.user.domain.entity.InvestmentProfileSector;

@Repository
public interface InvestmentProfileSectorRepository extends JpaRepository<InvestmentProfileSector, Long> {
    
    // 투자성향별 관심섹터 조회
    List<InvestmentProfileSector> findByInvestmentProfileProfileId(Long profileId);
    
    // 섹터별 관심 투자성향 조회
    List<InvestmentProfileSector> findBySectorSectorId(Long sectorId);
    
    // 특정 투자성향과 섹터의 매핑 조회
    Optional<InvestmentProfileSector> findByInvestmentProfileProfileIdAndSectorSectorId(Long profileId, Long sectorId);
}
