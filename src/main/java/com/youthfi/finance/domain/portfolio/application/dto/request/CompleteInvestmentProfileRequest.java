package com.youthfi.finance.domain.portfolio.application.dto.request;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "투자성향 설문 완료 요청")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteInvestmentProfileRequest {

    @Schema(description = "투자성향 유형", example = "CONSERVATIVE")
    private InvestmentProfile.InvestmentProfileType investmentProfile;

    @Schema(description = "투자가능 자산", example = "10000000.00")
    private BigDecimal availableAssets;

    @Schema(description = "투자 목표", example = "RETIREMENT")
    private InvestmentProfile.InvestmentGoal investmentGoal;

    @Schema(description = "관심섹터명 목록", example = "[\"전기전자\", \"통신\", \"바이오\"]")
    private List<String> interestedSectorNames;
}
