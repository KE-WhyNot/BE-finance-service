package com.youthfi.finance.domain.portfolio.infra;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.youthfi.finance.domain.ai.infra.GcpAuthenticationService;
import com.youthfi.finance.domain.portfolio.application.dto.response.InvestmentProfileResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;

class LLMApiClientIntegrationTest {

    private RestTemplate restTemplate;
    private MockRestServiceServer server;
    private GcpAuthenticationService gcpAuth;
    private LLMApiClient client;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        server = MockRestServiceServer.createServer(restTemplate);
        gcpAuth = org.mockito.Mockito.mock(GcpAuthenticationService.class);
        org.mockito.Mockito.when(gcpAuth.getCloudRunAuthHeader()).thenReturn("Bearer test-token");

        client = new LLMApiClient(restTemplate, gcpAuth);

        try {
            var f = LLMApiClient.class.getDeclaredField("llmApiUrl");
            f.setAccessible(true);
            f.set(client, "http://localhost/api/v1/portfolio");
        } catch (Exception ignored) {}
    }

    @Test
    void requestPortfolioRecommendation_setsSecurityHeaders_andParsesResponse() {
        server.expect(MockRestRequestMatchers.requestTo("http://localhost/api/v1/portfolio"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.header("Authorization", "Bearer test-token"))
                .andExpect(MockRestRequestMatchers.header("X-Request-Source", "finance-service"))
                .andRespond(MockRestResponseCreators.withSuccess(
                        "{\"portfolioId\":1,\"userId\":\"user-1\",\"recommendedStocks\":[],\"createdAt\":\"2025-01-01T10:00:00\",\"updatedAt\":\"2025-01-01T10:00:00\"}",
                        MediaType.APPLICATION_JSON
                ));

        InvestmentProfileResponse dummy = new InvestmentProfileResponse(
                1L,
                "user-1",
                InvestmentProfile.InvestmentProfileType.CONSERVATIVE,
                BigDecimal.TEN,
                InvestmentProfile.InvestmentGoal.RETIREMENT,
                List.of("TECH"),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );
        PortfolioResponse res = client.requestPortfolioRecommendation(dummy);
        assertThat(res).isNotNull();
    }
}


