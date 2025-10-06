package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "관심종목 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterestStockResponse {

    @Schema(description = "관심종목 ID", example = "1")
    private Long interestStockId;

    @Schema(description = "사용자 ID", example = "1234567890")
    private String userId;

    @Schema(description = "종목코드", example = "005930")
    private String stockId;

    @Schema(description = "종목명", example = "삼성전자")
    private String stockName;

    @Schema(description = "섹터명", example = "기술")
    private String sectorName;

    @Schema(description = "등록일시", example = "2025-01-01T10:30:00")
    private LocalDateTime createdAt;
}
