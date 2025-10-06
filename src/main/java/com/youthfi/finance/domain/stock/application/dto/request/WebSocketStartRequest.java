package com.youthfi.finance.domain.stock.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "WebSocket 시작 요청")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketStartRequest {

    @Schema(description = "종목코드 목록", example = "[\"005930\", \"000660\", \"035420\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "종목코드 목록은 필수입니다.")
    private List<String> stockCodes;
}
