package com.youthfi.finance.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "llm")
public class LLMApiProperties {

    private Api api = new Api();

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
