package com.youthfi.finance.domain.portfolio.application.usecase;

import com.youthfi.finance.domain.portfolio.domain.service.InvestmentProfileService;
import com.youthfi.finance.domain.portfolio.domain.service.PortfolioService;
import com.youthfi.finance.domain.portfolio.domain.service.PortfolioStockService;
import com.youthfi.finance.domain.portfolio.infra.LLMService;
import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
import com.youthfi.finance.domain.portfolio.domain.entity.PortfolioStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioRecommendationUseCase {

    private final InvestmentProfileService investmentProfileService;
    private final PortfolioService portfolioService;
    private final PortfolioStockService portfolioStockService;
    private final LLMService llmService;

    /**
     * 투자성향 정보를 바탕으로 AI가 추천하는 포트폴리오를 생성합니다.
     * 기존 투자성향이 없으면 예외를 던집니다.
     * AI 추천 결과를 받아 포트폴리오와 종목을 생성 및 저장합니다.
     *
     * @param userId 사용자 ID
     * @return 생성된 포트폴리오 엔터티
     * @throws RuntimeException 투자성향 정보가 없을 경우
     */

    @Transactional
    public Portfolio generatePortfolioRecommendation(Long userId) {
        InvestmentProfile investmentProfile = investmentProfileService.getInvestmentProfileByUserId(userId)
                .orElseThrow(() -> new RuntimeException("투자성향 정보가 없습니다. 먼저 설문을 완료해주세요."));

        Map<String, Object> recommendation = llmService.recommendPortfolio(investmentProfile, investmentProfile.getUser());

        Portfolio portfolio = portfolioService.createPortfolio(
            userId,
            (String) recommendation.get("portfolioName"),
            (BigDecimal) recommendation.get("allocationStocks"),
            (BigDecimal) recommendation.get("allocationSavings"),
            (BigDecimal) recommendation.get("expected1YrReturn")
        );

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> recommendedStocks = (List<Map<String, Object>>) recommendation.get("recommendedStocks");
        for (Map<String, Object> stockInfo : recommendedStocks) {
            String stockId = (String) stockInfo.get("stockId");
            BigDecimal allocationPct = (BigDecimal) stockInfo.get("allocationPct");
            portfolioStockService.addStockToPortfolio(portfolio.getPortfolioId(), stockId, allocationPct);
        }

        return portfolio;
    }

    /**
     * 특정 사용자의 모든 포트폴리오 추천 결과 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 포트폴리오 리스트
     */

    public List<Portfolio> getMyPortfolioRecommendations(Long userId) {
        return portfolioService.findPortfoliosByUserId(userId);
    }

    /**
     * 특정 사용자의 가장 최근 생성된 포트폴리오 추천 결과를 조회합니다.
     * 없으면 예외를 발생시킵니다.
     *
     * @param userId 사용자 ID
     * @return 최신 포트폴리오 엔터티
     * @throws RuntimeException 추천 포트폴리오 없을 경우
     */

    public Portfolio getMyLatestPortfolioRecommendation(Long userId) {
        List<Portfolio> portfolios = getMyPortfolioRecommendations(userId);
        if (portfolios.isEmpty()) {
            throw new RuntimeException("추천 포트폴리오가 없습니다. 먼저 포트폴리오를 생성해주세요.");
        }
        return portfolios.stream()
                .max(java.util.Comparator.comparing(com.youthfi.finance.domain.portfolio.domain.entity.Portfolio::getCreatedAt))
                .orElseThrow(() -> new RuntimeException("추천 포트폴리오를 찾을 수 없습니다."));
    }

    /**
     * 사용자의 특정 포트폴리오 상세 정보를 조회합니다.
     * 소유권 검사를 수행하며, 포트폴리오 정보, 포함된 종목 리스트와
     * 총 배분율, 종목 개수를 맵 형태로 반환합니다.
     *
     * @param userId 사용자 ID
     * @param portfolioId 포트폴리오 ID
     * @return 포트폴리오 상세 정보 맵
     * @throws RuntimeException 포트폴리오 미존재 시
     * @throws SecurityException 권한 없을 시
     */

    public Map<String, Object> getPortfolioDetails(Long userId, Long portfolioId) {
        Portfolio portfolio = portfolioService.findPortfolioById(portfolioId)
                .orElseThrow(() -> new RuntimeException("포트폴리오를 찾을 수 없습니다: " + portfolioId));

        if (!portfolio.getUser().getUserId().equals(userId)) {
            throw new SecurityException("해당 포트폴리오에 접근할 권한이 없습니다.");
        }

        List<PortfolioStock> portfolioStocks = portfolioStockService.getStocksByPortfolioId(portfolioId);

        return Map.of(
            "portfolio", portfolio,
            "stocks", portfolioStocks,
            "totalAllocationPct", portfolioStockService.getTotalAllocationPct(portfolioId),
            "stockCount", portfolioStockService.getStockCountByPortfolioId(portfolioId)
        );
    }

    /**
     * 사용자의 기존 포트폴리오 추천 결과를 모두 삭제하고
     * 새로 AI 추천 포트폴리오를 생성합니다.
     *
     * @param userId 사용자 ID
     * @return 새로 생성된 포트폴리오 엔터티
     */

    @Transactional
    public Portfolio regeneratePortfolioRecommendation(Long userId) {
        List<Portfolio> existingPortfolios = getMyPortfolioRecommendations(userId);
        for (Portfolio portfolio : existingPortfolios) {
            portfolioService.deletePortfolio(portfolio.getPortfolioId());
        }
        return generatePortfolioRecommendation(userId);
    }

    /**
     * 사용자가 포트폴리오 추천 생성 조건을 충족하는지 여부를 반환합니다.
     * 기본적으로 투자성향 정보 존재 여부를 판단합니다.
     *
     * @param userId 사용자 ID
     * @return 생성 가능 여부
     */

    public boolean canGenerateRecommendation(Long userId) {
        return investmentProfileService.existsInvestmentProfile(userId);
    }

    /**
     * 사용자의 포트폴리오 추천 생성 이력을 요약된 리스트 형태로 반환합니다.
     *
     * @param userId 사용자 ID
     * @return 포트폴리오 추천 이력 리스트 (Map 형태)
     */
    
    public List<Map<String, Object>> getPortfolioRecommendationHistory(Long userId) {
        List<Portfolio> portfolios = getMyPortfolioRecommendations(userId);
        return portfolios.stream()
                .map(portfolio -> Map.<String, Object>of(
                    "portfolioId", portfolio.getPortfolioId(),
                    "portfolioName", portfolio.getPortfolioName(),
                    "allocationStocks", portfolio.getAllocationStocks(),
                    "allocationSavings", portfolio.getAllocationSavings(),
                    "expected1YrReturn", portfolio.getExpected1YrReturn(),
                    "createdAt", portfolio.getCreatedAt(),
                    "stockCount", portfolioStockService.getStockCountByPortfolioId(portfolio.getPortfolioId())
                ))
                .toList();
    }
}


