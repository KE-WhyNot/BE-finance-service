package com.youthfi.finance.global.config;

import com.youthfi.finance.global.util.StockFrontendWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    
    private final StockFrontendWebSocketHandler stockFrontendWebSocketHandler;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(stockFrontendWebSocketHandler, "/ws/realtime")
                .setAllowedOrigins("http://localhost:3000", "http://localhost:8081", "https://finance.youth-fi.com")
                .setAllowedOriginPatterns("*");
    }
}

