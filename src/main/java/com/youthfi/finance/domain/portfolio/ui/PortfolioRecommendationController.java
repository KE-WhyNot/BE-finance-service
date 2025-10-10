package com.youthfi.finance.domain.portfolio.ui;

import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.domain.portfolio.application.mapper.PortfolioMapper;
import com.youthfi.finance.domain.portfolio.application.usecase.PortfolioUseCase;
import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.security.SecurityUtils;
import com.youthfi.finance.global.swagger.BaseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/portfolio-recommendation")
@RequiredArgsConstructor
@Tag(name = "PortfolioRecommendation", description = "AI 포트폴리오 추천 API")
public class PortfolioRecommendationController implements BaseApi {

    private final PortfolioUseCase portfolioRecommendationUseCase;
    private final PortfolioMapper portfolioMapper;

    /**
     * 사용자의 모든 AI 추천 포트폴리오 목록을 조회합니다.
     */
    @Operation(summary = "내 최신 포트폴리오 추천", description = "사용자의 최신 AI 추천 포트폴리오를 조회합니다.")
    @GetMapping("/my")
    public BaseResponse<PortfolioResponse> getMyPortfolioRecommendations() {
        String userId = SecurityUtils.getCurrentUserId();
        Portfolio latest = portfolioRecommendationUseCase.getMyLatestPortfolio(userId);
        return BaseResponse.onSuccess(portfolioMapper.toPortfolioResponse(latest));
    }

    /**
     * 사용자가 새로운 포트폴리오 추천을 받을 수 있는지 확인합니다.
     * 투자성향 프로필이 설정되어 있는지, 최근 추천 제한 등을 체크합니다.
     */
    @Operation(summary = "추천 생성 가능 여부 확인", description = "사용자가 새로운 포트폴리오 추천을 받을 수 있는지 확인합니다.")
    @GetMapping("/can-generate")
    public BaseResponse<Boolean> canGenerateRecommendation() {
        String userId = SecurityUtils.getCurrentUserId();
        boolean canGenerate = portfolioRecommendationUseCase.canGenerateRecommendation(userId);
        return BaseResponse.onSuccess(canGenerate);
    }

    /**
     * 사용자의 포트폴리오 추천 이력을 조회합니다.
     * 과거 추천된 포트폴리오들의 요약 정보와 생성 시점을 포함합니다.

     */
    @Operation(summary = "포트폴리오 추천 이력", description = "사용자의 포트폴리오 추천 생성 이력을 조회합니다.")
    @GetMapping("/my/history")
    public BaseResponse<List<Map<String, Object>>> getPortfolioRecommendationHistory() {
        String userId = SecurityUtils.getCurrentUserId();
        List<Map<String, Object>> history = portfolioRecommendationUseCase.getPortfolioRecommendationHistory(userId);
        return BaseResponse.onSuccess(history);
    }
}


