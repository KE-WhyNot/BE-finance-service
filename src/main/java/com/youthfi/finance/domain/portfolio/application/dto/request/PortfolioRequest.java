package com.youthfi.finance.domain.portfolio.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 포트폴리오 요청 DTO
 */
public record PortfolioRequest(
    @NotBlank(message = "사용자 ID는 필수입니다.")
    String userId,
    
    @NotBlank(message = "포트폴리오 이름은 필수입니다.")
    @Size(max = 100, message = "포트폴리오 이름은 100자를 초과할 수 없습니다.")
    String portfolioName,
    
    @NotNull(message = "투자 금액은 필수입니다.")
    Long investmentAmount,
    
    @NotBlank(message = "투자 성향은 필수입니다.")
    String riskTolerance,
    
    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
    String description
) {
    public PortfolioRequest {
        if (userId != null) {
            userId = userId.trim();
        }
        if (portfolioName != null) {
            portfolioName = portfolioName.trim();
        }
        if (description != null) {
            description = description.trim();
        }
    }
}
