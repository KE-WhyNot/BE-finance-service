package com.youthfi.finance.domain.portfolio.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "포트폴리오 생성 요청")
public record CreatePortfolioRequest(
    @Schema(description = "포트폴리오 이름", example = "나의 첫 포트폴리오")
    String portfolioName,

    @Schema(description = "52주 최고 자산", example = "12500000.00")
    BigDecimal highestValue,

    @Schema(description = "52주 최저 자산", example = "8200000.00")
    BigDecimal lowestValue
) {}
