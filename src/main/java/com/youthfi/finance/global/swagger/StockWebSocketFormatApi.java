package com.youthfi.finance.global.swagger;

import com.youthfi.finance.domain.stock.application.dto.response.StockWebSocketResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "실시간 주식 WebSocket", description = "서버-프론트 WebSocket 실시간 데이터 포맷 안내")

@RestController
@RequestMapping("/ws")
public class StockWebSocketFormatApi {
    @Operation(
        summary = "WebSocket 연결 가이드",
        description = "웹소켓 연결 방법과 데이터 포맷을 안내합니다."
    )
    @GetMapping("/realtime/guide")
    public Map<String, Object> websocketGuide() {
        Map<String, Object> guide = new HashMap<>();
        guide.put("websocketUrl", "ws://localhost:8081/ws/realtime");
        guide.put("connectionMethod", "JavaScript WebSocket API 사용");
        guide.put("exampleCode", """
            const ws = new WebSocket('ws://localhost:8081/ws/realtime');
            ws.onopen = () => console.log('연결 성공');
            ws.onmessage = (event) => console.log('데이터 수신:', event.data);
            ws.onerror = (error) => console.error('에러:', error);
            """);
        guide.put("dataFormat", new StockWebSocketResponse(
                "005930",      // symbol
                84500,         // stckPrpr (현재가)
                -200,          // prdyVrss (전일대비)
                -0.23,         // prdyCtrt (전일대비율)
                84000,         // stckLwpr (최저가)
                85000,         // stckHgpr (최고가)
                "2025-09-24T15:00:00"  // timestamp
        ));
        return guide;
    }
    
    @Operation(
        summary = "WebSocket 실시간 데이터 포맷",
        description = "실제 수신되는 데이터의 JSON 형식을 확인할 수 있습니다."
    )
    @GetMapping("/realtime/format")
    public StockWebSocketResponse websocketFormatDoc() {
        return new StockWebSocketResponse(
                "005930",      // symbol
                84500,         // stckPrpr (현재가)
                -200,          // prdyVrss (전일대비)
                -0.23,         // prdyCtrt (전일대비율)
                84000,         // stckLwpr (최저가)
                85000,         // stckHgpr (최고가)
                "2025-09-24T15:00:00"  // timestamp
        );
    }
}
