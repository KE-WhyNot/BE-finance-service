package com.youthfi.finance.domain.portfolio.ui;

import java.util.concurrent.CompletableFuture;
import org.springframework.web.bind.annotation.*;
import com.youthfi.finance.domain.portfolio.application.dto.request.CompleteInvestmentProfileRequest;
import com.youthfi.finance.domain.portfolio.application.dto.request.UpdateInvestmentProfileRequest;
import com.youthfi.finance.domain.portfolio.application.dto.response.InvestmentProfileResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.domain.portfolio.application.usecase.InvestmentProfileUseCase;
import com.youthfi.finance.domain.portfolio.application.usecase.PortfolioUseCase;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.security.SecurityUtils;
import com.youthfi.finance.global.swagger.BaseApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user/investment-profile")
@RequiredArgsConstructor
@Tag(name = "InvestmentProfile", description = "투자성향 프로필 관리 API")
public class InvestmentProfileController implements BaseApi {

    private final InvestmentProfileUseCase investmentProfileUseCase;
    private final PortfolioUseCase portfolioRecommendationUseCase;

    /**
     * 투자성향 설문을 완료하거나 새 프로필을 생성 및 저장
     */

    @Operation(
        summary = "투자성향 설문 완료", 
        description = "투자성향 설문을 완료, 새 프로필을 생성 및 저장합니다.\n\n" +
                     "**투자성향 유형(investmentProfile):** CONSERVATIVE, CONSERVATIVE_SEEKING, RISK_NEUTRAL, AGGRESSIVE, VERY_AGGRESSIVE\n\n" +
                     "**투자 목표(investmentGoal):** EDUCATION, LIVING_EXPENSES, HOUSE_PURCHASE, ASSET_GROWTH, DEBT_REPAYMENT\n\n" +
                     "**감당가능 손실(lossTolerance):** NO_LOSS, TEN_PERCENT, THIRTY_PERCENT, FIFTY_PERCENT, SEVENTY_PERCENT, FULL_AMOUNT\n\n" +
                     "**금융 이해도(financialKnowledge):** VERY_LOW, LOW, MEDIUM, HIGH, VERY_HIGH\n\n" +
                     "**기대 이익(expectedProfit):** ONE_FIFTY_PERCENT, TWO_HUNDRED_PERCENT, TWO_FIFTY_PERCENT, THREE_HUNDRED_PERCENT_PLUS\n\n" +
                     "**관심섹터(interestedSectorNames):** 화학, 제약, 전기·전자, 운송장비·부품, 기타금융, 기계·장비, 금속, 건설, IT 서비스"
    )
    @PostMapping("/complete")
    public BaseResponse<InvestmentProfileResponse> completeInvestmentProfile(
            @RequestBody CompleteInvestmentProfileRequest request) {
        String userId = SecurityUtils.getCurrentUserId();
        InvestmentProfileResponse profile = investmentProfileUseCase.completeInvestmentProfile(userId, request);
        return BaseResponse.onSuccess(profile);
    }

    /**
     * 특정 사용자 투자성향 프로필 정보를 조회
     */

    @Operation(summary = "내 투자성향 조회", description = "사용자의 투자성향 프로필 정보를 조회합니다.")
    @GetMapping("/my")
    public BaseResponse<InvestmentProfileResponse> getMyInvestmentProfile() {
        String userId = SecurityUtils.getCurrentUserId();
        InvestmentProfileResponse profile = investmentProfileUseCase.getMyInvestmentProfile(userId);
        return BaseResponse.onSuccess(profile);
    }

    /**
     * 특정 사용자 투자성향 프로필 수정
     */

    @Operation(
        summary = "내 투자성향 수정", 
        description = "사용자의 투자성향 프로필을 수정합니다.\n\n" +
                    "**투자성향 유형(investmentProfile):** CONSERVATIVE, CONSERVATIVE_SEEKING, RISK_NEUTRAL, AGGRESSIVE, VERY_AGGRESSIVE\n\n" +
                     "**투자 목표(investmentGoal):** EDUCATION, LIVING_EXPENSES, HOUSE_PURCHASE, ASSET_GROWTH, DEBT_REPAYMENT\n\n" +
                     "**감당가능 손실(lossTolerance):** NO_LOSS, TEN_PERCENT, THIRTY_PERCENT, FIFTY_PERCENT, SEVENTY_PERCENT, FULL_AMOUNT\n\n" +
                     "**금융 이해도(financialKnowledge):** VERY_LOW, LOW, MEDIUM, HIGH, VERY_HIGH\n\n" +
                     "**기대 이익(expectedProfit):** ONE_FIFTY_PERCENT, TWO_HUNDRED_PERCENT, TWO_FIFTY_PERCENT, THREE_HUNDRED_PERCENT_PLUS\n\n" +
                     "**관심섹터(interestedSectorNames):** 화학, 제약, 전기·전자, 운송장비·부품, 기타금융, 기계·장비, 금속, 건설, IT 서비스"
    )
    @PatchMapping("/my")
    public BaseResponse<InvestmentProfileResponse> updateMyInvestmentProfile(
            @RequestBody UpdateInvestmentProfileRequest request) {
        String userId = SecurityUtils.getCurrentUserId();
        InvestmentProfileResponse profile = investmentProfileUseCase.updateMyInvestmentProfile(userId, request);
        return BaseResponse.onSuccess(profile);
    }

    /**
     * 사용자 투자성향 설문을 완료여부 조회
     */
    
    @Operation(summary = "투자성향 완료 여부 확인", description = "사용자가 투자성향 설문을 완료했는지 확인합니다.")
    @GetMapping("/exists")
    public BaseResponse<Boolean> hasCompletedInvestmentProfile() {
        String userId = SecurityUtils.getCurrentUserId();
        boolean exists = investmentProfileUseCase.hasCompletedInvestmentProfile(userId);
        return BaseResponse.onSuccess(exists);
    }

    /**
     * 투자성향 프로필을 LLM Domain으로 전송하여 포트폴리오 추천 요청, 응답 생성.
     */
    
    @Operation(summary = "LLM 포트폴리오 추천 요청", description = "투자성향 프로필을 기반으로 포트폴리오를 생성하고 저장합니다. 비동기 처리로 즉시 응답합니다.")
    @PostMapping("/send-to-llm")
    public BaseResponse<String> sendProfileToLLM() {
        String userId = SecurityUtils.getCurrentUserId();
        
        // 백에서 포트폴리오 생성
        CompletableFuture.runAsync(() -> {
            try {
                portfolioRecommendationUseCase.generateAiPortfolioRecommendation(userId);
                log.info("포트폴리오 생성 완료: userId={}", userId);
            } catch (Exception e) {
                log.error("포트폴리오 생성 실패: userId={}, error={}", userId, e.getMessage(), e);
            }
        });
        
        return BaseResponse.onSuccess("포트폴리오 생성이 시작되었습니다. 완료 시 포트폴리오 목록에서 확인하실 수 있습니다.");
    }
}


