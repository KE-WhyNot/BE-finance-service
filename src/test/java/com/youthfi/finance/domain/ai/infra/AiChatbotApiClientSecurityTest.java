package com.youthfi.finance.domain.ai.infra;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.youthfi.finance.domain.ai.application.dto.request.ChatRequest;
import com.youthfi.finance.domain.ai.application.dto.response.ChatResponse;
import com.youthfi.finance.global.config.properties.AiApiProperties;

class AiChatbotApiClientSecurityTest {

    private RestTemplate restTemplate;
    private MockRestServiceServer server;
    private GcpAuthenticationService gcpAuth;
    private AiApiProperties props;
    private AiChatbotApiClient client;

    @BeforeEach
    void setUp() throws Exception {
        restTemplate = new RestTemplate();
        server = MockRestServiceServer.createServer(restTemplate);
        gcpAuth = org.mockito.Mockito.mock(GcpAuthenticationService.class);
        org.mockito.Mockito.when(gcpAuth.getCloudRunAuthHeader()).thenReturn("Bearer test-token");

        props = new AiApiProperties();
        AiApiProperties.Chatbot chatbot = new AiApiProperties.Chatbot();
        chatbot.setApiUrl("http://localhost");
        props.setChatbot(chatbot);

        client = new AiChatbotApiClient(restTemplate, gcpAuth, props);
    }

    @Test
    void sendChatRequest_setsAuthorizationHeader() {
        server.expect(MockRestRequestMatchers.requestTo("http://localhost/api/v1/chat"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.header("Authorization", "Bearer test-token"))
                .andExpect(MockRestRequestMatchers.header("X-Request-Source", "finance-service"))
                .andRespond(MockRestResponseCreators.withSuccess(
                        "{\"replyText\":\"ok\",\"actionType\":\"display_info\",\"success\":true}",
                        MediaType.APPLICATION_JSON
                ));

        ChatRequest req = new ChatRequest("hi", "u1", "s1");
        ChatResponse res = client.sendChatRequest(req);
        assertThat(res.success()).isTrue();
        server.verify();
    }
}