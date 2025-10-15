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
        // 환경변수가 있으면 우선 사용, 없으면 설정 파일 값 사용
        String envBaseUrl = System.getenv("AI_BASE_URL");
        if (envBaseUrl != null && !envBaseUrl.isBlank()) {
            this.baseUrl = envBaseUrl;
        }
        
        System.out.println("AiApiProperties 초기화 완료");
        System.out.println("Base URL: " + baseUrl);
        System.out.println("Chatbot URL: " + (chatbot != null ? chatbot.getApiUrl() : "null"));
        System.out.println("Portfolio URL: " + (portfolio != null ? portfolio.getApiUrl() : "null"));
        System.out.println("GCP Target Audience: " + (gcp != null ? gcp.getTargetAudience() : "null"));
    }
    
    private String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }
    
    private String getRequiredEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("필수 환경변수가 설정되지 않았습니다: " + name);
        }
        return value;
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
        
        public String getTargetAudience() {
            if (targetAudience == null || targetAudience.trim().isEmpty()) {
                String value = System.getenv("AI_TARGET_AUDIENCE");
                return (value != null && !value.isBlank()) ? value : targetAudience;
            }
            return targetAudience;
        }

        @Getter
        @Setter
        public static class ServiceAccount {
            private String key;
            private String email;
            
            public String getKey() {
                if (key == null || key.trim().isEmpty()) {
                    String value = System.getenv("GCP_SA_KEY_JSON");
                    return (value != null && !value.isBlank()) ? value : key;
                }
                return key;
            }
            
            public String getEmail() {
                if (email == null || email.trim().isEmpty()) {
                    String value = System.getenv("AI_GCP_SERVICE_ACCOUNT_EMAIL");
                    return (value != null && !value.isBlank()) ? value : email;
                }
                return email;
            }
        }
    }
}
