package com.youthfi.finance.domain.user.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.user.domain.entity.InvestmentProfile;

@Repository
public interface InvestmentProfileRepository extends JpaRepository<InvestmentProfile, Long> {
    
    // 사용자별 투자성향 조회
    Optional<InvestmentProfile> findByUserUserId(Long userId);
    
    // 투자성향별 조회
    List<InvestmentProfile> findByInvestmentProfile(InvestmentProfile.InvestmentProfileType investmentProfile);
    
    // 투자목표별 조회
    List<InvestmentProfile> findByInvestmentGoal(InvestmentProfile.InvestmentGoal investmentGoal);
}
