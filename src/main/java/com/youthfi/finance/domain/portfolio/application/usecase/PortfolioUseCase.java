package com.youthfi.finance.domain.portfolio.application.usecase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youthfi.finance.domain.portfolio.application.dto.response.InvestmentProfileResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioRiskAnalysisResponse;
import com.youthfi.finance.domain.portfolio.application.mapper.PortfolioMapper;
import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
import com.youthfi.finance.domain.portfolio.domain.service.InvestmentProfileService;
import com.youthfi.finance.domain.portfolio.domain.service.PortfolioRiskService;
import com.youthfi.finance.domain.portfolio.domain.service.PortfolioService;
import com.youthfi.finance.domain.portfolio.domain.service.PortfolioStockService;
import com.youthfi.finance.domain.portfolio.infra.LLMApiClient;
import com.youthfi.finance.global.exception.PortfolioException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioUseCase {

    private final InvestmentProfileService investmentProfileService;
    private final PortfolioService portfolioService;
    private final PortfolioStockService portfolioStockService;
    private final LLMApiClient llmApiClient;
    private final PortfolioRiskService portfolioRiskCalculator;
    private final PortfolioMapper portfolioMapper;

    @Transactional
    public Portfolio generatePortfolioRecommendation(String userId) {
        InvestmentProfile investmentProfile = investmentProfileService.getInvestmentProfileByUserId(userId)
                .orElseThrow(() -> PortfolioException.investmentProfileNotFound());

        InvestmentProfileResponse profileResponse = portfolioMapper.toInvestmentProfileResponse(investmentProfile);
        PortfolioResponse recommendation = llmApiClient.requestPortfolioRecommendation(profileResponse);

        PortfolioRiskAnalysisResponse riskAnalysis = portfolioRiskCalculator.calculatePortfolioRisk(
                recommendation.recommendedStocks(),
                BigDecimal.valueOf(10_000_000)
        );

        Portfolio portfolio = portfolioService.createPortfolio(
                userId,
                "AI 추천 포트폴리오",
                riskAnalysis.highestValue(),
                riskAnalysis.lowestValue()
        );

        for (PortfolioResponse.RecommendedStock stock : recommendation.recommendedStocks()) {
            portfolioStockService.addStockToPortfolio(portfolio.getPortfolioId(), stock.stockId(), stock.allocationPct());
        }

        return portfolio;
    }

    /**
     * AI 포트폴리오 추천 생성 (Controller용)
     */
    @Transactional
    public PortfolioResponse generateAiPortfolioRecommendation(String userId) {
        // 1. AI 포트폴리오 추천 생성
        Portfolio portfolio = generatePortfolioRecommendation(userId);
        
        // 2. PortfolioResponse로 변환하여 반환
        return portfolioService.convertToPortfolioResponse(portfolio);
    }


    public List<Portfolio> getMyPortfolioRecommendations(String userId) {
        return portfolioService.findPortfoliosByUserId(userId);
    }


    public Portfolio getMyLatestPortfolio(String userId) {
        List<Portfolio> portfolios = getMyPortfolioRecommendations(userId);
        PortfolioException.validatePortfolioExists(portfolios.isEmpty() ? null : portfolios.get(0), null);
        return portfolios.stream()
                .max(java.util.Comparator.comparing(Portfolio::getCreatedAt))
                .orElseThrow(() -> PortfolioException.portfolioNotFound(null));
    }


    public boolean canGenerateRecommendation(String userId) {
        return investmentProfileService.existsInvestmentProfile(userId);
    }


    public List<Map<String, Object>> getPortfolioRecommendationHistory(String userId) {
        List<Portfolio> portfolios = getMyPortfolioRecommendations(userId);
        return portfolios.stream()
                .map(portfolio -> Map.<String, Object>of(
                        "portfolioId", portfolio.getPortfolioId(),
                        "portfolioName", portfolio.getPortfolioName(),
                        "highestValue", portfolio.getHighestValue(),
                        "lowestValue", portfolio.getLowestValue(),
                        "createdAt", portfolio.getCreatedAt(),
                        "stockCount", portfolioStockService.getStockCountByPortfolioId(portfolio.getPortfolioId())
                ))
                .toList();
    }
}

