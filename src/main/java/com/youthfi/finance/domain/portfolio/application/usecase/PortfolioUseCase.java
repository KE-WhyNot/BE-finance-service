package com.youthfi.finance.domain.portfolio.application.usecase;

import com.youthfi.finance.domain.portfolio.application.dto.response.InvestmentProfileResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioRiskAnalysisResponse;
import com.youthfi.finance.domain.portfolio.application.mapper.PortfolioMapper;
import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
import com.youthfi.finance.domain.portfolio.domain.entity.PortfolioStock;
import com.youthfi.finance.domain.portfolio.domain.service.InvestmentProfileService;
import com.youthfi.finance.domain.portfolio.domain.service.PortfolioRiskService;
import com.youthfi.finance.domain.portfolio.domain.service.PortfolioService;
import com.youthfi.finance.domain.portfolio.domain.service.PortfolioStockService;
import com.youthfi.finance.domain.portfolio.infra.LLMApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
                .orElseThrow(() -> new RuntimeException("투자성향 정보가 없습니다. 먼저 설문을 완료해주세요."));

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


    public List<Portfolio> getMyPortfolioRecommendations(String userId) {
        return portfolioService.findPortfoliosByUserId(userId);
    }


    public Portfolio getMyLatestPortfolio(String userId) {
        List<Portfolio> portfolios = getMyPortfolioRecommendations(userId);
        if (portfolios.isEmpty()) {
            throw new RuntimeException("추천 포트폴리오가 없습니다. 먼저 포트폴리오를 생성해주세요.");
        }
        return portfolios.stream()
                .max(java.util.Comparator.comparing(Portfolio::getCreatedAt))
                .orElseThrow(() -> new RuntimeException("추천 포트폴리오를 찾을 수 없습니다."));
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

