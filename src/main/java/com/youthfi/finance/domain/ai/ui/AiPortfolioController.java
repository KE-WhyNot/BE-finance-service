package com.youthfi.finance.domain.ai.ui;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youthfi.finance.domain.portfolio.application.dto.request.PortfolioRequest;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.domain.portfolio.application.usecase.PortfolioUseCase;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.security.SecurityUtils;
import com.youthfi.finance.global.swagger.BaseApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "AI 포트폴리오", description = "AI 포트폴리오 추천 관련 API")
@RestController
@RequestMapping("/api/ai/portfolio")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "X-User-Id")
public class AiPortfolioController implements BaseApi {

    private final PortfolioUseCase portfolioUseCase;

    /**
     * AI 포트폴리오 추천
     */
    @Operation(summary = "AI 포트폴리오 추천", description = "사용자의 투자성향을 기반으로 AI가 포트폴리오를 추천합니다.")
    @PostMapping("/recommend")
    public BaseResponse<PortfolioResponse> recommendPortfolio(@Valid @RequestBody PortfolioRequest request) {
        String userId = SecurityUtils.getCurrentUserId();
        PortfolioResponse response = portfolioUseCase.generateAiPortfolioRecommendation(userId);
        return BaseResponse.onSuccess(response);
    }
}
