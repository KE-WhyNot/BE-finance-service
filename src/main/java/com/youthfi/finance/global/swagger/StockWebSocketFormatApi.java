package com.youthfi.finance.global.swagger;

import com.youthfi.finance.domain.stock.application.dto.response.StockWebSocketResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "실시간 주식 WebSocket", description = "서버-프론트 WebSocket 실시간 데이터 포맷 안내")

@RestController

@RequestMapping("/ws/stock")

public class StockWebSocketFormatApi {
    @Operation(
        summary = "WebSocket 실시간 데이터 포맷",
        description = "서버와 프론트가 WebSocket으로 통신할 때, 아래와 같은 JSON 메시지 형식으로 실시간 데이터가 전달됩니다."
    )
    @GetMapping("/format")
    public StockWebSocketResponse websocketFormatDoc() {
        return new StockWebSocketResponse(
                "005930",
                List.of(84500, 84600, 84700, 84800),
                List.of(84400, 84300, 84200, 84100),
                List.of(100, 200, 150, 180),
                List.of(120, 210, 160, 190),
                84500,
                -200,
                -0.23,
                84000,
                85000,
                "2025-09-24T15:00:00"
        );
    }
}
