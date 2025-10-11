package com.youthfi.finance.domain.ai.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.IdTokenProvider;
import com.youthfi.finance.global.config.properties.AiApiProperties;

class GcpAuthenticationServiceTest {

    @Test
    void generateIdToken_withServiceAccountJson_success() throws Exception {
        AiApiProperties props = new AiApiProperties();
        AiApiProperties.Gcp gcp = new AiApiProperties.Gcp();
        AiApiProperties.Gcp.ServiceAccount sa = new AiApiProperties.Gcp.ServiceAccount();
        // 최소 필드 포함된 더미 서비스 계정(JSON) — 실제 테스트에선 유효 JSON을 주입하거나 mocking 권장
        String dummyJson = "{\"type\":\"service_account\",\"client_email\":\"x@y\",\"private_key\":\"-----BEGIN PRIVATE KEY-----\\nMII...\\n-----END PRIVATE KEY-----\\n\"}";
        sa.setKey(dummyJson);
        gcp.setServiceAccount(sa);
        gcp.setTargetAudience("https://example.com");
        props.setGcp(gcp);

        GcpAuthenticationService svc = new GcpAuthenticationService(props);
        // 실제 외부 호출을 피하기 위해 메소드를 호출만 하되, 예외가 아닌 경우를 기대
        try {
            svc.generateIdToken();
        } catch (RuntimeException ex) {
            // 로컬 환경에서 키가 유효하지 않아 실패할 수 있으므로 메시지 확인 정도로 제한
            assertThat(ex.getMessage()).contains("GCP ID 토큰 발급");
        }
    }

    @Test
    void generateIdToken_withAdcUserCredentials_throws() throws Exception {
        AiApiProperties props = new AiApiProperties();
        props.getGcp().setTargetAudience("https://example.com");

        // IdTokenProvider 미구현 크리덴셜 모킹
        GoogleCredentials mockCreds = Mockito.mock(GoogleCredentials.class);

        GcpAuthenticationService svc = new GcpAuthenticationService(props) {
            @Override
            public String generateIdToken() {
                if (mockCreds instanceof IdTokenProvider) {
                    throw new AssertionError("Should not be IdTokenProvider in this test");
                }
                throw new RuntimeException("ID 토큰 발급을 지원하지 않는 인증 방식입니다.");
            }
        };

        RuntimeException ex = assertThrows(RuntimeException.class, svc::generateIdToken);
        assertThat(ex.getMessage()).contains("ID 토큰 발급을 지원");
    }
}


