package com.youthfi.finance.domain.portfolio.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfileSector;

@Repository
public interface InvestmentProfileSectorRepository extends JpaRepository<InvestmentProfileSector, Long> {
    List<InvestmentProfileSector> findByInvestmentProfileProfileId(Long profileId);
    List<InvestmentProfileSector> findBySectorSectorId(Long sectorId);
    Optional<InvestmentProfileSector> findByInvestmentProfileProfileIdAndSectorSectorId(Long profileId, Long sectorId);
}


