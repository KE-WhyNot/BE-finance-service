package com.youthfi.finance.domain.ai.infra;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.youthfi.finance.domain.ai.application.dto.request.ChatRequest;
import com.youthfi.finance.domain.ai.application.dto.response.ChatResponse;
import com.youthfi.finance.global.config.properties.AiApiProperties;

@ExtendWith(MockitoExtension.class)
class AiChatbotApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GcpAuthenticationService gcpAuthenticationService;

    private AiApiProperties aiApiProperties;

    @InjectMocks
    private AiChatbotApiClient client;

    @BeforeEach
    void setUp() {
        aiApiProperties = new AiApiProperties();
        AiApiProperties.Chatbot chatbot = new AiApiProperties.Chatbot();
        chatbot.setApiUrl("https://example.test");
        aiApiProperties.setChatbot(chatbot);

        // 수동 주입 (InjectMocks가 final 필드여서 반영 안될 수 있어 setter 방식 대신 리플렉션 사용)
        try {
            var f = AiChatbotApiClient.class.getDeclaredField("aiApiProperties");
            f.setAccessible(true);
            f.set(client, aiApiProperties);
        } catch (Exception ignored) {}

        when(gcpAuthenticationService.getCloudRunAuthHeader()).thenReturn("Bearer test-token");
    }

    @Test
    void sendChatRequest_success() {
        ChatRequest request = new ChatRequest("hello", "user-1", "sess-1");
        ChatResponse expected = ChatResponse.success("ok", "display_info", null, null, null);

        when(restTemplate.exchange(
                eq("https://example.test/api/v1/chat"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ChatResponse.class)
        )).thenReturn(ResponseEntity.ok(expected));

        ChatResponse actual = client.sendChatRequest(request);
        assertThat(actual.replyText()).isEqualTo("ok");
        assertThat(actual.success()).isTrue();
    }
}