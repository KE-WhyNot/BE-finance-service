package com.youthfi.finance.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ai")
public class AiApiProperties {
    
    private String baseUrl; // 공통 AI 서비스 URL
    private Chatbot chatbot;
    private Portfolio portfolio;
    private Gcp gcp = new Gcp();
    
    public AiApiProperties() {
        System.out.println("AiApiProperties 생성자 호출");
        this.chatbot = new Chatbot(this);
        this.portfolio = new Portfolio(this);
    }
    
    @PostConstruct
    public void init() {
        System.out.println("AiApiProperties 초기화 완료");
        System.out.println("Base URL: " + baseUrl);
        System.out.println("Chatbot URL: " + (chatbot != null ? chatbot.getApiUrl() : "null"));
        System.out.println("Portfolio URL: " + (portfolio != null ? portfolio.getApiUrl() : "null"));
        System.out.println("GCP Target Audience: " + (gcp != null ? gcp.getTargetAudience() : "null"));
    }

    @Getter
    @Setter
    public static class Chatbot {
        private String apiUrl;
        private final AiApiProperties parent;
        
        public Chatbot(AiApiProperties parent) {
            this.parent = parent;
            System.out.println("Chatbot 생성자 호출");
        }
        
        public void setApiUrl(String apiUrl) {
            System.out.println("Chatbot.setApiUrl 호출: " + apiUrl);
            this.apiUrl = apiUrl;
        }
        
        public String getApiUrl() {
            // apiUrl이 설정되지 않았으면 baseUrl 사용
            if (apiUrl == null || apiUrl.trim().isEmpty()) {
                return parent != null ? parent.getBaseUrl() : null;
            }
            return apiUrl;
        }
        private int timeout = 30000;
        private Retry retry = new Retry();

        @Getter
        @Setter
        public static class Retry {
            private int maxAttempts = 3;
            private long delay = 1000;
        }
    }

    @Getter
    @Setter
    public static class Portfolio {
        private String apiUrl;
        private final AiApiProperties parent;
        private int timeout = 30000;
        private Retry retry = new Retry();
        
        public Portfolio(AiApiProperties parent) {
            this.parent = parent;
            System.out.println("Portfolio 생성자 호출");
        }
        
        public String getApiUrl() {
            // apiUrl이 설정되지 않았으면 baseUrl 사용
            if (apiUrl == null || apiUrl.trim().isEmpty()) {
                return parent != null ? parent.getBaseUrl() : null;
            }
            return apiUrl;
        }

        @Getter
        @Setter
        public static class Retry {
            private int maxAttempts = 3;
            private long delay = 1000;
        }
    }

    @Getter
    @Setter
    public static class Gcp {
        private ServiceAccount serviceAccount = new ServiceAccount();
        private String targetAudience;

        @Getter
        @Setter
        public static class ServiceAccount {
            private String key;
            private String email;
        }
    }
}
