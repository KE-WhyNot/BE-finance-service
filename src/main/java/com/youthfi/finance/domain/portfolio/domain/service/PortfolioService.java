package com.youthfi.finance.domain.portfolio.domain.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
import com.youthfi.finance.domain.portfolio.domain.repository.PortfolioRepository;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.user.domain.repository.UserRepository;
import com.youthfi.finance.global.exception.PortfolioException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    /**
     * 새로운 포트폴리오를 생성합니다.
     * 사용자 존재 여부 확인 후 생성하며,
     * 포트폴리오 정보는 빌더 패턴으로 구성합니다.
     */

    public Portfolio createPortfolio(String userId, String portfolioName,
                                   BigDecimal highestValue, BigDecimal lowestValue) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> PortfolioException.userNotFound(userId));

        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .portfolioName(portfolioName)
                .highestValue(highestValue)
                .lowestValue(lowestValue)
                .build();
        return portfolioRepository.save(portfolio);
    }

    /**
     * 포트폴리오 ID로 포트폴리오 정보를 조회합니다.
     */

    public Optional<Portfolio> findPortfolioById(Long portfolioId) {
        return portfolioRepository.findById(portfolioId);
    }

    /**
     * 사용자 ID 기반으로 해당 사용자의 모든 포트폴리오를 조회합니다.
     */

    public List<Portfolio> findPortfoliosByUserId(String userId) {
        return portfolioRepository.findByUserUserId(userId);
    }

    /**
     * 포트폴리오 정보를 수정합니다.
     * 포트폴리오 존재 여부 확인 후, 내부 update 메서드로 변경내용 반영합니다.
     */

    public Portfolio updatePortfolio(Long portfolioId, String portfolioName,
                                   BigDecimal highestValue, BigDecimal lowestValue) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> PortfolioException.portfolioNotFound(portfolioId));

        portfolio.updatePortfolio(portfolioName, highestValue, lowestValue);
        return portfolio;
    }

    /**
     * Portfolio 엔티티를 PortfolioResponse로 변환
     */
    public PortfolioResponse convertToPortfolioResponse(Portfolio portfolio) {
        List<PortfolioResponse.RecommendedStock> recommendedStocks = portfolio.getPortfolioStocks().stream()
                .map(ps -> new PortfolioResponse.RecommendedStock(
                        ps.getStock().getStockId(),
                        ps.getStock().getStockName(),
                        ps.getAllocationPct(),
                        ps.getStock().getSector().getSectorName(),
                        "AI 추천"
                ))
                .toList();

        return new PortfolioResponse(
                portfolio.getPortfolioId(),
                portfolio.getUser().getUserId(),
                recommendedStocks,
                portfolio.getCreatedAt(),
                portfolio.getUpdatedAt()
        );
    }
}


