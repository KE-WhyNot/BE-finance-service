package com.youthfi.finance.domain.portfolio.application.usecase;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youthfi.finance.domain.portfolio.application.dto.response.CompleteInvestmentProfileResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.InvestmentProfileResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioRiskAnalysisResponse;
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

    /**
     * AI 포트폴리오 추천 생성
     */
    @Transactional
    public PortfolioResponse generateAiPortfolioRecommendation(String userId) {
        // 1. 투자 프로필 조회
        InvestmentProfile investmentProfile = investmentProfileService.getInvestmentProfileByUserId(userId)
                .orElseThrow(() -> PortfolioException.investmentProfileNotFound());

        // 2. LLM API 호출하여 CompleteInvestmentProfileResponse 받음
        InvestmentProfileResponse profileResponse = investmentProfileService.toInvestmentProfileResponse(investmentProfile);
        CompleteInvestmentProfileResponse llmResponse = llmApiClient.requestPortfolioRecommendation(profileResponse);

        // 3. 위험도 분석 수행 (null 가드)
        List<CompleteInvestmentProfileResponse.RecommendedStock> safeStocks =
                java.util.Optional.ofNullable(llmResponse.recommendedStocks())
                        .orElse(java.util.Collections.emptyList());

        PortfolioRiskAnalysisResponse riskAnalysis = portfolioRiskCalculator.calculatePortfolioRisk(
                safeStocks,
                investmentProfile.getAvailableAssets()
        );

        // 4. 포트폴리오 엔티티 생성 및 저장
        Portfolio portfolio = portfolioService.createPortfolio(
                userId,
                "AI 추천 포트폴리오",
                riskAnalysis.highestValue(),
                riskAnalysis.lowestValue()
        );

        // 5. 포트폴리오에 주식 추가 (null 가드 적용 리스트 사용)
        for (CompleteInvestmentProfileResponse.RecommendedStock stock : safeStocks) {
            portfolioStockService.addStockToPortfolio(portfolio.getPortfolioId(), stock.stockId(), stock.allocationPct());
        }

        // 6. CompleteInvestmentProfileResponse.RecommendedStock을 PortfolioResponse.RecommendedStock으로 변환 (null 가드 적용 리스트 사용)
        List<PortfolioResponse.RecommendedStock> portfolioRecommendedStocks = safeStocks.stream()
                .map(stock -> new PortfolioResponse.RecommendedStock(
                        stock.stockId(),
                        stock.stockName(),
                        stock.allocationPct(),
                        stock.sectorName(),
                        stock.reason()
                ))
                .toList();

        // 7. 종합된 PortfolioResponse 반환 (LLM 응답 + 위험도 분석 + 저장된 포트폴리오 정보)
        return new PortfolioResponse(
                portfolio.getPortfolioId(),
                userId,
                portfolioRecommendedStocks,
                llmResponse.allocationSavings(), // LLM에서 받은 예적금 비율 사용
                riskAnalysis.highestValue(), // 위험도 분석 결과
                riskAnalysis.lowestValue(),  // 위험도 분석 결과
                portfolio.getCreatedAt(),
                portfolio.getUpdatedAt()
        );
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
                .collect(java.util.stream.Collectors.toList());
    }
}

