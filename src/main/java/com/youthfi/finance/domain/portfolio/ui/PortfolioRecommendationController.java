package com.youthfi.finance.domain.portfolio.ui;

import com.youthfi.finance.domain.portfolio.application.usecase.PortfolioRecommendationUseCase;
import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
import com.youthfi.finance.global.common.BaseResponse;
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

    private final PortfolioRecommendationUseCase portfolioRecommendationUseCase;

    /**
     * AI 기반 투자 포트폴리오 추천을 생성합니다.
     *
     * @param userId 사용자 ID (필수)
     * @return 생성된 Portfolio 엔터티를 포함하는 공통 응답 객체
     * @throws RuntimeException 추천 생성 실패 시 (UseCase 내부 예외)
     */
    
    @Operation(summary = "AI 포트폴리오 추천 생성", description = "투자성향을 기반으로 AI가 추천하는 포트폴리오를 생성합니다.")
    @PostMapping("/generate")
    public BaseResponse<Portfolio> generatePortfolioRecommendation(@RequestParam Long userId) {
        Portfolio portfolio = portfolioRecommendationUseCase.generatePortfolioRecommendation(userId);
        return BaseResponse.onSuccess(portfolio);
    }

    /**
     * 사용자의 모든 AI 추천 포트폴리오 목록을 조회합니다.
     *
     * @param userId 사용자 ID (필수)
     * @return 사용자의 모든 Portfolio 엔터티 목록을 포함하는 공통 응답 객체
     * @throws RuntimeException 포트폴리오 조회 실패 시 (UseCase 내부 예외)
     */
    @Operation(summary = "내 포트폴리오 추천 목록", description = "사용자의 모든 AI 추천 포트폴리오 목록을 조회합니다.")
    @GetMapping("/my")
    public BaseResponse<List<Portfolio>> getMyPortfolioRecommendations(@RequestParam Long userId) {
        List<Portfolio> portfolios = portfolioRecommendationUseCase.getMyPortfolioRecommendations(userId);
        return BaseResponse.onSuccess(portfolios);
    }

    /**
     * 사용자의 가장 최근 AI 추천 포트폴리오를 조회합니다.
     *
     * @param userId 사용자 ID (필수)
     * @return 가장 최근 생성된 Portfolio 엔터티를 포함하는 공통 응답 객체
     * @throws RuntimeException 포트폴리오 조회 실패 시 (UseCase 내부 예외)
     */
    @Operation(summary = "내 최신 포트폴리오 추천", description = "사용자의 가장 최근 AI 추천 포트폴리오를 조회합니다.")
    @GetMapping("/my/latest")
    public BaseResponse<Portfolio> getMyLatestPortfolioRecommendation(@RequestParam Long userId) {
        Portfolio portfolio = portfolioRecommendationUseCase.getMyLatestPortfolioRecommendation(userId);
        return BaseResponse.onSuccess(portfolio);
    }

    /**
     * 특정 포트폴리오의 상세 정보를 조회합니다.
     * 포트폴리오 기본 정보와 추천된 종목들의 상세 데이터를 포함합니다.
     *
     * @param userId 사용자 ID (필수)
     * @param portfolioId 포트폴리오 ID (필수)
     * @return 포트폴리오 상세 정보 맵을 포함하는 공통 응답 객체
     * @throws RuntimeException 포트폴리오 조회 실패 시 (UseCase 내부 예외)
     */
    @Operation(summary = "포트폴리오 상세 조회", description = "특정 포트폴리오의 상세 정보와 추천 종목들을 조회합니다.")
    @GetMapping("/{portfolioId}/details")
    public BaseResponse<Map<String, Object>> getPortfolioDetails(
            @RequestParam Long userId, 
            @PathVariable Long portfolioId) {
        Map<String, Object> details = portfolioRecommendationUseCase.getPortfolioDetails(userId, portfolioId);
        return BaseResponse.onSuccess(details);
    }

    /**
     * 기존 포트폴리오를 삭제하고 새로운 AI 추천 포트폴리오를 재생성합니다.
     *
     * @param userId 사용자 ID (필수)
     * @return 새로 생성된 Portfolio 엔터티를 포함하는 공통 응답 객체
     * @throws RuntimeException 재생성 실패 시 (UseCase 내부 예외)
     */
    @Operation(summary = "포트폴리오 재생성", description = "기존 포트폴리오를 삭제하고 새로운 AI 추천 포트폴리오를 생성합니다.")
    @PostMapping("/regenerate")
    public BaseResponse<Portfolio> regeneratePortfolioRecommendation(@RequestParam Long userId) {
        Portfolio portfolio = portfolioRecommendationUseCase.regeneratePortfolioRecommendation(userId);
        return BaseResponse.onSuccess(portfolio);
    }

    /**
     * 사용자가 새로운 포트폴리오 추천을 받을 수 있는지 확인합니다.
     * 투자성향 프로필이 설정되어 있는지, 최근 추천 제한 등을 체크합니다.
     *
     * @param userId 사용자 ID (필수)
     * @return 추천 가능 여부를 나타내는 Boolean 값을 포함하는 공통 응답 객체
     * @throws RuntimeException 확인 실패 시 (UseCase 내부 예외)
     */
    @Operation(summary = "추천 생성 가능 여부 확인", description = "사용자가 새로운 포트폴리오 추천을 받을 수 있는지 확인합니다.")
    @GetMapping("/can-generate")
    public BaseResponse<Boolean> canGenerateRecommendation(@RequestParam Long userId) {
        boolean canGenerate = portfolioRecommendationUseCase.canGenerateRecommendation(userId);
        return BaseResponse.onSuccess(canGenerate);
    }

    /**
     * 사용자의 포트폴리오 추천 이력을 조회합니다.
     * 과거 추천된 포트폴리오들의 요약 정보와 생성 시점을 포함합니다.
     *
     * @param userId 사용자 ID (필수)
     * @return 포트폴리오 추천 이력 맵 목록을 포함하는 공통 응답 객체
     * @throws RuntimeException 이력 조회 실패 시 (UseCase 내부 예외)
     */
    @Operation(summary = "포트폴리오 추천 이력", description = "사용자의 포트폴리오 추천 생성 이력을 조회합니다.")
    @GetMapping("/my/history")
    public BaseResponse<List<Map<String, Object>>> getPortfolioRecommendationHistory(@RequestParam Long userId) {
        List<Map<String, Object>> history = portfolioRecommendationUseCase.getPortfolioRecommendationHistory(userId);
        return BaseResponse.onSuccess(history);
    }
}


