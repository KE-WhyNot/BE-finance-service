package com.youthfi.finance.domain.portfolio.application.usecase;

import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import com.youthfi.finance.domain.portfolio.domain.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioUseCase {

    private final PortfolioService portfolioService;
    private final UserRepository userRepository; // 사용자 존재 여부 확인용

    @Transactional
    public Portfolio createMyPortfolio(Long userId, String portfolioName,
                                     BigDecimal allocationStocks, BigDecimal allocationSavings,
                                     BigDecimal expected1YrReturn) {
        BigDecimal totalAllocation = allocationStocks.add(allocationSavings);
        if (totalAllocation.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new IllegalArgumentException("주식과 예금 배분율의 합은 100%여야 합니다.");
        }
        if (expected1YrReturn.compareTo(BigDecimal.valueOf(-100)) < 0) {
            throw new IllegalArgumentException("예상 수익률은 -100% 이상이어야 합니다.");
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));

        return portfolioService.createPortfolio(userId, portfolioName,
                allocationStocks, allocationSavings, expected1YrReturn);
    }

    public List<Portfolio> getMyPortfolios(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));

        return portfolioService.findPortfoliosByUserId(userId);
    }

    public Portfolio getMyPortfolio(Long userId, Long portfolioId) {
        Portfolio portfolio = portfolioService.findPortfolioById(portfolioId)
                .orElseThrow(() -> new RuntimeException("포트폴리오를 찾을 수 없습니다: " + portfolioId));

        if (!portfolio.getUser().getUserId().equals(userId)) {
            throw new SecurityException("해당 포트폴리오에 접근할 권한이 없습니다.");
        }
        return portfolio;
    }

    @Transactional
    public Portfolio updateMyPortfolio(Long userId, Long portfolioId, String portfolioName,
                                     BigDecimal allocationStocks, BigDecimal allocationSavings,
                                     BigDecimal expected1YrReturn) {
        Portfolio portfolio = getMyPortfolio(userId, portfolioId);

        BigDecimal totalAllocation = allocationStocks.add(allocationSavings);
        if (totalAllocation.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new IllegalArgumentException("주식과 예금 배분율의 합은 100%여야 합니다.");
        }

        return portfolioService.updatePortfolio(portfolioId, portfolioName,
                allocationStocks, allocationSavings, expected1YrReturn);
    }

    @Transactional
    public void deleteMyPortfolio(Long userId, Long portfolioId) {
        getMyPortfolio(userId, portfolioId);
        portfolioService.deletePortfolio(portfolioId);
    }
}


