package com.youthfi.finance.domain.stock.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "배당일정 조회 요청")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DividendScheduleRequest {

    @Schema(description = "종목코드", example = "005930", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "종목코드는 필수입니다.")
    private String stockCode;
}
