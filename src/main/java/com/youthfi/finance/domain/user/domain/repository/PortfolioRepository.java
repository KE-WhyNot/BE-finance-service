package com.youthfi.finance.domain.user.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youthfi.finance.domain.user.domain.entity.Portfolio;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    
    // 사용자별 포트폴리오 조회
    List<Portfolio> findByUserUserId(Long userId);
    
    // 포트폴리오명으로 조회
    Optional<Portfolio> findByPortfolioName(String portfolioName);
    
    // 사용자별 포트폴리오명으로 조회
    Optional<Portfolio> findByUserUserIdAndPortfolioName(Long userId, String portfolioName);
}
