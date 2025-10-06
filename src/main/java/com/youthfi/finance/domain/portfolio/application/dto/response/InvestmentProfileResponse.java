package com.youthfi.finance.domain.portfolio.application.dto.response;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "투자성향 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentProfileResponse {

    @Schema(description = "투자성향 프로필 ID", example = "1")
    private Long profileId;

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "투자성향 유형", example = "CONSERVATIVE")
    private InvestmentProfile.InvestmentProfileType investmentProfile;

    @Schema(description = "투자가능 자산", example = "10000000.00")
    private BigDecimal availableAssets;

    @Schema(description = "투자 목표", example = "RETIREMENT")
    private InvestmentProfile.InvestmentGoal investmentGoal;

    @Schema(description = "관심섹터명 목록", example = "[\"전기전자\", \"통신\", \"바이오\"]")
    private List<String> interestedSectors;

    @Schema(description = "생성일시", example = "2025-01-01T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-01-01T15:45:00")
    private LocalDateTime updatedAt;

}
