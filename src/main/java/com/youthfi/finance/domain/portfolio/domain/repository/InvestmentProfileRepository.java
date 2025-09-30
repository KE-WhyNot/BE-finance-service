package com.youthfi.finance.domain.portfolio.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;

@Repository
public interface InvestmentProfileRepository extends JpaRepository<InvestmentProfile, Long> {
    Optional<InvestmentProfile> findByUserUserId(Long userId);
    List<InvestmentProfile> findByInvestmentProfile(InvestmentProfile.InvestmentProfileType investmentProfile);
    List<InvestmentProfile> findByInvestmentGoal(InvestmentProfile.InvestmentGoal investmentGoal);
}


