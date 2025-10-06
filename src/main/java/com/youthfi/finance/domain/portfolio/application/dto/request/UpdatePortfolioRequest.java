package com.youthfi.finance.domain.portfolio.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "포트폴리오 수정 요청")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePortfolioRequest {

    @Schema(description = "포트폴리오 이름", example = "수정된 포트폴리오")
    private String portfolioName;

    @Schema(description = "52주 최고 자산", example = "13000000.00")
    private BigDecimal highestValue;

    @Schema(description = "52주 최저 자산", example = "7900000.00")
    private BigDecimal lowestValue;
}
