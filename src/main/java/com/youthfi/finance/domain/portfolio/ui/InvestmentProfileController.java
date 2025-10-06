package com.youthfi.finance.domain.portfolio.ui;

import com.youthfi.finance.domain.portfolio.application.dto.request.CompleteInvestmentProfileRequest;
import com.youthfi.finance.domain.portfolio.application.dto.request.UpdateInvestmentProfileRequest;
import com.youthfi.finance.domain.portfolio.application.dto.response.InvestmentProfileResponse;
import com.youthfi.finance.domain.portfolio.application.mapper.PortfolioMapper;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.domain.portfolio.application.usecase.InvestmentProfileUseCase;
import com.youthfi.finance.domain.portfolio.application.usecase.PortfolioUseCase;
import com.youthfi.finance.domain.portfolio.domain.entity.Portfolio;
import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.domain.portfolio.infra.LLMApiClient;
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
@RequestMapping("/api/user/investment-profile")
@RequiredArgsConstructor
@Tag(name = "InvestmentProfile", description = "투자성향 프로필 관리 API")
public class InvestmentProfileController implements BaseApi {

    private final InvestmentProfileUseCase investmentProfileUseCase;
    private final PortfolioMapper portfolioMapper;
    private final PortfolioUseCase portfolioRecommendationUseCase;

    /**
     * 투자성향 설문을 완료하거나 새 프로필을 생성 및 저장
     */

    @Operation(summary = "투자성향 설문 완료", description = "투자성향 설문을 완료, 새 프로필을 생성 및 저장합니다.")
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

    @Operation(summary = "내 투자성향 수정", description = "사용자의 투자성향 프로필 정보를 수정합니다.")
    @PutMapping("/my")
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
    
    @Operation(summary = "LLM 포트폴리오 추천 요청", description = "투자성향 프로필을 기반으로 포트폴리오를 생성하고 저장합니다.")
    @PostMapping("/send-to-llm")
    public BaseResponse<PortfolioResponse> sendProfileToLLM() {
        String userId = SecurityUtils.getCurrentUserId();
        Portfolio portfolio = portfolioRecommendationUseCase.generatePortfolioRecommendation(userId);
        return BaseResponse.onSuccess(portfolioMapper.toPortfolioResponse(portfolio));
    }
}


