package com.youthfi.finance.domain.portfolio.domain.service;

import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.portfolio.domain.repository.PortfolioRepository;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    @Transactional
    public Portfolio createPortfolio(Long userId, String portfolioName,
                                   BigDecimal allocationStocks, BigDecimal allocationSavings,
                                   BigDecimal expected1YrReturn) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));

        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .portfolioName(portfolioName)
                .allocationStocks(allocationStocks)
                .allocationSavings(allocationSavings)
                .expected1YrReturn(expected1YrReturn)
                .build();
        return portfolioRepository.save(portfolio);
    }

    public Optional<Portfolio> findPortfolioById(Long portfolioId) {
        return portfolioRepository.findById(portfolioId);
    }

    public List<Portfolio> findPortfoliosByUserId(Long userId) {
        return portfolioRepository.findByUserUserId(userId);
    }

    @Transactional
    public Portfolio updatePortfolio(Long portfolioId, String portfolioName,
                                   BigDecimal allocationStocks, BigDecimal allocationSavings,
                                   BigDecimal expected1YrReturn) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("포트폴리오를 찾을 수 없습니다: " + portfolioId));

        portfolio.updatePortfolio(portfolioName, allocationStocks, allocationSavings, expected1YrReturn);
        return portfolio;
    }

    @Transactional
    public void deletePortfolio(Long portfolioId) {
        portfolioRepository.deleteById(portfolioId);
    }
}


