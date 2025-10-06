package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "WebSocket 상태 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketStatusResponse {

    @Schema(description = "활성 연결 수", example = "4")
    private int activeConnectionCount;

    @Schema(description = "상태 메시지", example = "WebSocket 연결 정상")
    private String statusMessage;
}
