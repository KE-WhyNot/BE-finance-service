package com.youthfi.finance.domain.portfolio.application.dto.request;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "투자성향 설문 완료 요청")
public record CompleteInvestmentProfileRequest(
    @Schema(
        description = "투자성향 유형 - 사용자의 투자 성향을 나타냅니다", example = "CONSERVATIVE", allowableValues = {"CONSERVATIVE", "CONSERVATIVE_SEEKING", "RISK_NEUTRAL", "AGGRESSIVE", "VERY_AGGRESSIVE"}
    )
    InvestmentProfile.InvestmentProfileType investmentProfile,

    @Schema(description = "투자가능 자산", example = "10000000.00")
    BigDecimal availableAssets,

    @Schema(description = "투자 목표", example = "EDUCATION", allowableValues = {"EDUCATION", "LIVING_EXPENSES", "HOUSE_PURCHASE", "ASSET_GROWTH", "DEBT_REPAYMENT"})
    InvestmentProfile.InvestmentGoal investmentGoal,

    @Schema(description = "감당가능 손실", example = "TEN_PERCENT", allowableValues = {"NO_LOSS", "TEN_PERCENT", "THIRTY_PERCENT", "FIFTY_PERCENT", "SEVENTY_PERCENT", "FULL_AMOUNT"})
    InvestmentProfile.LossTolerance lossTolerance,

    @Schema(description = "금융 이해도", example = "MEDIUM", allowableValues = {"VERY_LOW", "LOW", "MEDIUM", "HIGH", "VERY_HIGH"})
    InvestmentProfile.FinancialKnowledge financialKnowledge,

    @Schema(description = "기대 이익", example = "TWO_HUNDRED_PERCENT", allowableValues = {"ONE_FIFTY_PERCENT", "TWO_HUNDRED_PERCENT", "TWO_FIFTY_PERCENT", "THREE_HUNDRED_PERCENT_PLUS"})
    InvestmentProfile.ExpectedProfit expectedProfit,

    @Schema(description = "관심섹터명 목록", example = "[\"전기·전자\", \"건설\", \"IT 서비스\"]", allowableValues = {"화학", "제약", "전기·전자", "운송장비·부품", "기타금융", "기계·장비", "금속", "건설", "IT 서비스"})
    List<String> interestedSectorNames
) {}
