package com.youthfi.finance.domain.portfolio.domain.repository;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfileSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentProfileSectorRepository extends JpaRepository<InvestmentProfileSector, Long> {
    List<InvestmentProfileSector> findByInvestmentProfile(InvestmentProfile investmentProfile);
}