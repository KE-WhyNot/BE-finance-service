package com.youthfi.finance.domain.portfolio.infra;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.youthfi.finance.domain.ai.infra.GcpAuthenticationService;
import com.youthfi.finance.domain.portfolio.application.dto.response.InvestmentProfileResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.global.config.properties.LLMApiProperties;
import com.youthfi.finance.global.exception.PortfolioException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LLMApiClient {

    private final RestTemplate restTemplate;
    private final GcpAuthenticationService gcpAuthenticationService;
    private final LLMApiProperties llmApiProperties;

    /**
     * 투자성향 프로필을 AI 서비스로 전송하여 포트폴리오 추천 요청
     */
    public PortfolioResponse requestPortfolioRecommendation(InvestmentProfileResponse investmentProfile) {
        try {
            log.info("AI 서비스로 투자성향 프로필 전송 시작: profileId={}", investmentProfile.profileId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", gcpAuthenticationService.getCloudRunAuthHeader());
            headers.set("X-Request-Source", "finance-service");
            headers.set("X-Request-Type", "portfolio-recommendation");

            HttpEntity<InvestmentProfileResponse> request = new HttpEntity<>(investmentProfile, headers);

            ResponseEntity<PortfolioResponse> response = restTemplate.exchange(
                    llmApiProperties.getApi().getUrl(),
                    HttpMethod.POST,
                    request,
                    PortfolioResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("AI 응답 성공: profileId={}", investmentProfile.profileId());
                return response.getBody();
            } else {
                log.error("AI 응답 실패: status={}, profileId={}", response.getStatusCode(), investmentProfile.profileId());
                throw PortfolioException.llmApiConnectionFailed(new RuntimeException("AI API 호출 실패: " + response.getStatusCode()));
            }
        } catch (Exception e) {
            log.error("AI API 호출 중 오류 발생: profileId={}, error={}", investmentProfile.profileId(), e.getMessage(), e);
            throw PortfolioException.llmApiConnectionFailed(e);
        }
    }

    public boolean checkConnection() {
        try {
            String healthUrl = llmApiProperties.getApi().getUrl().replace("/api/v1/portfolio", "/health");
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.warn("AI 서비스 연결 확인 실패: {}", e.getMessage());
            return false;
        }
    }
}
