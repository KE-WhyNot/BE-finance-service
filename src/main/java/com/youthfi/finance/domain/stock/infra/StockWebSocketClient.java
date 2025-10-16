package com.youthfi.finance.domain.stock.infra;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import com.youthfi.finance.domain.stock.application.dto.response.StockWebSocketResponse;
import com.youthfi.finance.global.exception.StockException;
import lombok.extern.slf4j.Slf4j;
import java.util.function.Consumer;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;

@Slf4j
public class StockWebSocketClient extends WebSocketClient {
    private final String appkey;
    private final String approvalKey;
    private final String trId;
    private final List<String> trKeys; // 여러 종목 지원
    private final Consumer<StockWebSocketResponse> messageConsumer;

    public StockWebSocketClient(URI serverUri, String appkey, String approvalKey, String trId, List<String> trKeys, Consumer<StockWebSocketResponse> messageConsumer) {
        super(serverUri);
        this.appkey = appkey;
        this.approvalKey = approvalKey;
        this.trId = trId;
        this.trKeys = trKeys;
        this.messageConsumer = messageConsumer;
    }

    public String getAppkey() {
        return appkey;
    }

    public String getTrId() {
        return trId;
    }

    public List<String> getTrKeys() {
        return trKeys;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.info("WebSocket 연결 성공! (appkey={}, trId={})", appkey, trId);
        for (String trKey : trKeys) {
            String subscribeMsg = String.format(
                "{\"header\":{\"approval_key\":\"%s\",\"custtype\":\"P\",\"tr_type\":\"1\",\"content-type\":\"utf-8\"},\"body\":{\"input\":{\"tr_id\":\"%s\",\"tr_key\":\"%s\"}}}",
                approvalKey, trId, trKey
            );
            send(subscribeMsg);
        }
    }

    @Override
    public void onMessage(String message) {
        // 구분자 기반 파싱
        if (message.contains("|") && message.contains("^")) {
            try {
                String[] parts = message.split("\\|");
                if (parts.length > 1) {
                    String trId = parts[1];
                    String[] fields = parts[3].split("\\^");
                    if ("H0STCNT0".equals(trId)) {
                        // 체결가 파싱
                        String stckPrpr = fields.length > 2 ? fields[2] : "0";
                        String prdyVrss = fields.length > 4 ? fields[4] : "0";
                        String prdyCtrt = fields.length > 5 ? fields[5] : "0";
                        String stckLwpr = fields.length > 7 ? fields[7] : "0";
                        String stckHgpr = fields.length > 8 ? fields[8] : "0";
                        String symbol = fields.length > 0 ? fields[0] : "";
                        StockWebSocketResponse dto = new StockWebSocketResponse(
                                symbol,
                                Integer.parseInt(stckPrpr),
                                Integer.parseInt(prdyVrss),
                                Double.parseDouble(prdyCtrt),
                                Integer.parseInt(stckLwpr),
                                Integer.parseInt(stckHgpr),
                                LocalDateTime.now().toString()
                        );
                        if (messageConsumer != null) {
                            messageConsumer.accept(dto);
                        }
                        
                    }
                }
            } catch (Exception e) {
                log.error("실시간 데이터 수신(파싱실패): {}", message, e);
                throw StockException.kisApiResponseError(e);
            }
        } else {
            log.debug("실시간 데이터 수신: {}", message);
            // 핑퐁 메시지도 브로드캐스트 (연결 상태 확인용)
            if (messageConsumer != null && message.contains("PINGPONG")) {
                try {
                    // 핑퐁 메시지를 특별한 형식으로 브로드캐스트
                    messageConsumer.accept(new StockWebSocketResponse(
                        "PINGPONG", 0, 0, 0.0, 0, 0, LocalDateTime.now().toString()
                    ));
                    log.info("핑퐁 메시지 브로드캐스트: {}", message);
                } catch (Exception e) {
                    log.error("핑퐁 메시지 브로드캐스트 실패: {}", message, e);
                }
            }
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("WebSocket 연결 종료: {} (code={}, remote={})", reason, code, remote);
    }

    @Override
    public void onError(Exception ex) {
        log.error("WebSocket 연결 오류 발생", ex);
        throw StockException.websocketConnectionFailed(ex);
    }
}