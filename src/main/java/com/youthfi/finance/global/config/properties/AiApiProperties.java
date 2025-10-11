package com.youthfi.finance.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ai")
public class AiApiProperties {

    private Chatbot chatbot = new Chatbot();
    private Portfolio portfolio = new Portfolio();
    private Gcp gcp = new Gcp();

    @Getter
    @Setter
    public static class Chatbot {
        private String apiUrl;
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
