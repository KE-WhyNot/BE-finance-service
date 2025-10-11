package com.youthfi.finance.domain.portfolio.application.dto.request;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "투자성향 설문 완료 요청")
public record CompleteInvestmentProfileRequest(
    @Schema(description = "투자성향 유형", example = "CONSERVATIVE")
    InvestmentProfile.InvestmentProfileType investmentProfile,

    @Schema(description = "투자가능 자산", example = "10000000.00")
    BigDecimal availableAssets,

    @Schema(description = "투자 목표", example = "EDUCATION")
    InvestmentProfile.InvestmentGoal investmentGoal,

    @Schema(description = "감당가능 손실", example = "TEN_PERCENT")
    InvestmentProfile.LossTolerance lossTolerance,

    @Schema(description = "금융 이해도", example = "MEDIUM")
    InvestmentProfile.FinancialKnowledge financialKnowledge,

    @Schema(description = "기대 이익", example = "TWO_HUNDRED_PERCENT")
    InvestmentProfile.ExpectedProfit expectedProfit,

    @Schema(description = "관심섹터명 목록", example = "[\"전기전자\", \"통신\", \"바이오\"]")
    List<String> interestedSectorNames
) {}
