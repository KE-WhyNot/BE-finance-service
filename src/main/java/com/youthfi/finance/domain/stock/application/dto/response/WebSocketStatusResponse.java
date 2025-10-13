package com.youthfi.finance.domain.stock.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WebSocket 상태 응답")
public record WebSocketStatusResponse(
    @Schema(description = "활성 연결 수", example = "4")
    int activeConnectionCount,

    @Schema(description = "상태 메시지", example = "WebSocket 연결 정상")
    String statusMessage
) {}
