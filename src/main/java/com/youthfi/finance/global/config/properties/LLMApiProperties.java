package com.youthfi.finance.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "llm")
public class LLMApiProperties {

    private Api api = new Api();
    
    @PostConstruct
    public void init() {
        // 환경변수가 있으면 우선 사용, 없으면 설정 파일 값 사용
        String envBaseUrl = System.getenv("LLM_API_URL");
        if (envBaseUrl != null && !envBaseUrl.isBlank()) {
            this.api.setUrl(envBaseUrl + "/api/v1/portfolio/enhanced");
        }
        
        String envTimeout = System.getenv("LLM_API_TIMEOUT");
        if (envTimeout != null && !envTimeout.isBlank()) {
            this.api.setTimeout(Integer.parseInt(envTimeout));
        }
        
        String envMaxAttempts = System.getenv("LLM_API_RETRY_MAX_ATTEMPTS");
        if (envMaxAttempts != null && !envMaxAttempts.isBlank()) {
            this.api.getRetry().setMaxAttempts(Integer.parseInt(envMaxAttempts));
        }
        
        String envDelay = System.getenv("LLM_API_RETRY_DELAY");
        if (envDelay != null && !envDelay.isBlank()) {
            this.api.getRetry().setDelay(Long.parseLong(envDelay));
        }
    }
    
    private String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }

    @Getter
    @Setter
    public static class Api {
        private String url;
        private int timeout = 30000;
        private Retry retry = new Retry();

        @Getter
        @Setter
        public static class Retry {
            private int maxAttempts = 3;
            private long delay = 1000;
        }
    }
}
