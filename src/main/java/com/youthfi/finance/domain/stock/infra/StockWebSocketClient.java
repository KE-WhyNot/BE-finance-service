package com.youthfi.finance.domain.stock.infra;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import com.youthfi.finance.domain.stock.application.dto.response.StockWebSocketResponse;
import java.util.function.Consumer;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;

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

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket 연결 성공! (appkey=" + appkey + ", trId=" + trId + ")");
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
                    if ("H0STASP0".equals(trId)) {
                        // 호가 파싱 (4쌍만, 잔량)
                        String askPrice1 = fields.length > 3 ? fields[3] : "0";
                        String askPrice2 = fields.length > 4 ? fields[4] : "0";
                        String askPrice3 = fields.length > 5 ? fields[5] : "0";
                        String askPrice4 = fields.length > 6 ? fields[6] : "0";
                        String bidPrice1 = fields.length > 13 ? fields[13] : "0";
                        String bidPrice2 = fields.length > 14 ? fields[14] : "0";
                        String bidPrice3 = fields.length > 15 ? fields[15] : "0";
                        String bidPrice4 = fields.length > 16 ? fields[16] : "0";
                        // 매도호가잔량1~4: 23~26, 매수호가잔량1~4: 33~36
                        String askQty1 = fields.length > 23 ? fields[23] : "0";
                        String askQty2 = fields.length > 24 ? fields[24] : "0";
                        String askQty3 = fields.length > 25 ? fields[25] : "0";
                        String askQty4 = fields.length > 26 ? fields[26] : "0";
                        String bidQty1 = fields.length > 33 ? fields[33] : "0";
                        String bidQty2 = fields.length > 34 ? fields[34] : "0";
                        String bidQty3 = fields.length > 35 ? fields[35] : "0";
                        String bidQty4 = fields.length > 36 ? fields[36] : "0";
                        List<Integer> askPrices = Arrays.asList(askPrice1, askPrice2, askPrice3, askPrice4).stream().map(Integer::parseInt).toList();
                        List<Integer> bidPrices = Arrays.asList(bidPrice1, bidPrice2, bidPrice3, bidPrice4).stream().map(Integer::parseInt).toList();
                        List<Integer> askQtys = Arrays.asList(askQty1, askQty2, askQty3, askQty4).stream().map(Integer::parseInt).toList();
                        List<Integer> bidQtys = Arrays.asList(bidQty1, bidQty2, bidQty3, bidQty4).stream().map(Integer::parseInt).toList();
                        String symbol = fields.length > 0 ? fields[0] : "";
                        StockWebSocketResponse dto = new StockWebSocketResponse(
                                symbol,
                                askPrices,
                                bidPrices,
                                askQtys,
                                bidQtys,
                                null,
                                null,
                                null,
                                null,
                                null,
                                LocalDateTime.now().toString()
                        );
                        if (messageConsumer != null) {
                            messageConsumer.accept(dto);
                        }
                        
                    } else if ("H0STCNT0".equals(trId)) {
                        // 체결가 파싱
                        String stckPrpr = fields.length > 2 ? fields[2] : "0";
                        String prdyVrss = fields.length > 4 ? fields[4] : "0";
                        String prdyCtrt = fields.length > 5 ? fields[5] : "0";
                        String stckLwpr = fields.length > 7 ? fields[7] : "0";
                        String stckHgpr = fields.length > 8 ? fields[8] : "0";
                        String symbol = fields.length > 0 ? fields[0] : "";
                        StockWebSocketResponse dto = new StockWebSocketResponse(
                                symbol,
                                null,
                                null,
                                null,
                                null,
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
                System.out.println("실시간 데이터 수신(파싱실패): " + message);
            }
        } else {
            System.out.println("실시간 데이터 수신: " + message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket 연결 종료: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}