package com.youthfi.finance.domain.portfolio.application.dto.request;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "투자성향 수정 요청")
public record UpdateInvestmentProfileRequest(
    @Schema(description = "투자성향 유형", example = "AGGRESSIVE")
    InvestmentProfile.InvestmentProfileType investmentProfile,

    @Schema(description = "투자가능 자산", example = "15000000.00")
    BigDecimal availableAssets,

    @Schema(description = "투자 목표", example = "HOUSE_PURCHASE")
    InvestmentProfile.InvestmentGoal investmentGoal,

    @Schema(description = "감당가능 손실", example = "THIRTY_PERCENT")
    InvestmentProfile.LossTolerance lossTolerance,

    @Schema(description = "금융 이해도", example = "HIGH")
    InvestmentProfile.FinancialKnowledge financialKnowledge,

    @Schema(description = "기대 이익", example = "TWO_FIFTY_PERCENT")
    InvestmentProfile.ExpectedProfit expectedProfit,

    @Schema(description = "관심섹터명 목록", example = "[\"전기전자\", \"통신\", \"바이오\"]")
    List<String> interestedSectorNames
) {}
