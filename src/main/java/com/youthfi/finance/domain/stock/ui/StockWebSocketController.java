package com.youthfi.finance.domain.stock.ui;

import com.youthfi.finance.domain.stock.application.dto.request.WebSocketStartRequest;
import com.youthfi.finance.domain.stock.application.dto.response.WebSocketStatusResponse;
import com.youthfi.finance.domain.stock.application.usecase.StockWebSocketUseCase;
import com.youthfi.finance.global.common.BaseResponse;
import com.youthfi.finance.global.swagger.BaseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock/ws")
@RequiredArgsConstructor
@Tag(name = "StockWebSocket", description = "주식 WebSocket 관리 API")
public class StockWebSocketController implements BaseApi {

    private final StockWebSocketUseCase stockWebSocketUseCase;

    @Operation(summary = "WebSocket 시작", description = "지정된 종목들에 대한 WebSocket 실시간 수신을 시작합니다.")
    @PostMapping("/start")
    public BaseResponse<String> startWebSocket(@Valid @RequestBody WebSocketStartRequest request) {
        stockWebSocketUseCase.startWebSocketsForAllKeysAndStocks(request.getStockCodes());
        return BaseResponse.onSuccess("OK");
    }
    
    @Operation(summary = "WebSocket 종료", description = "모든 WebSocket 연결을 종료합니다.")
    @PostMapping("/stop")
    public BaseResponse<String> stopWebSocket() {
        stockWebSocketUseCase.stopAllWebSockets();
        return BaseResponse.onSuccess("OK");
    }
    
    @Operation(summary = "WebSocket 상태 조회", description = "현재 활성화된 WebSocket 연결 상태를 조회합니다.")
    @GetMapping("/status")
    public BaseResponse<WebSocketStatusResponse> getWebSocketStatus() {
        int activeConnections = stockWebSocketUseCase.getActiveConnectionCount();
        
        WebSocketStatusResponse response = WebSocketStatusResponse.builder()
                .activeConnectionCount(activeConnections)
                .statusMessage(activeConnections > 0 ? "WebSocket 연결 정상" : "WebSocket 연결 없음")
                .build();
        
        return BaseResponse.onSuccess(response);
    }
}