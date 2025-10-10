package com.youthfi.finance.global.util;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.youthfi.finance.global.exception.StockException;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class StockFrontendWebSocketHandler extends TextWebSocketHandler {
    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("WebSocket 연결 성공: sessionId={}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("WebSocket 연결 종료: sessionId={}, status={}", session.getId(), status);
    }

    public static void broadcast(String json) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(json));
            } catch (Exception e) {
                log.error("WebSocket 메시지 전송 실패: sessionId={}", session.getId(), e);
                throw StockException.websocketConnectionFailed(e);
            }
        }
    }
}
