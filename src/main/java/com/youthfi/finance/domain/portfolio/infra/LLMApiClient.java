package com.youthfi.finance.domain.portfolio.infra;

import com.youthfi.finance.domain.portfolio.application.dto.response.InvestmentProfileResponse;
import com.youthfi.finance.domain.portfolio.application.dto.response.PortfolioResponse;
import com.youthfi.finance.global.exception.PortfolioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LLMApiClient {

    private final RestTemplate restTemplate;

    @Value("${llm.api.url:http://localhost:8081/api/llm/recommend}")
    private String llmApiUrl;
    
    @Value("${llm.api.timeout:30000}")
    private int timeout;

    /**
     * 투자성향 프로필을 LLM Domain으로 전송하여 포트폴리오 추천을 요청
     */

    public PortfolioResponse requestPortfolioRecommendation(InvestmentProfileResponse investmentProfile) {
        try {
            log.info("LLM Domain으로 투자성향 프로필 전송 시작: profileId={}", investmentProfile.profileId());
            
            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Request-Source", "finance-service");
            headers.set("X-Request-Type", "portfolio-recommendation");

            // HTTP 엔티티 생성 (DTO를 직접 사용)
            HttpEntity<InvestmentProfileResponse> request = new HttpEntity<>(investmentProfile, headers);
            
            // API 호출
            ResponseEntity<PortfolioResponse> response = restTemplate.exchange(
                    llmApiUrl,
                    HttpMethod.POST,
                    request,
                    PortfolioResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("LLM Domain 응답 성공: profileId={}", investmentProfile.profileId());
                return response.getBody();
            } else {
                log.error("LLM Domain 응답 실패: status={}, profileId={}", 
                         response.getStatusCode(), investmentProfile.profileId());
                throw PortfolioException.llmApiConnectionFailed(
                    new RuntimeException("LLM Domain API 호출 실패: " + response.getStatusCode()));
            }
            
        } catch (Exception e) {
            log.error("LLM Domain API 호출 중 오류 발생: profileId={}, error={}", 
                     investmentProfile.profileId(), e.getMessage(), e);
            throw PortfolioException.llmApiConnectionFailed(e);
        }
    }


    /**
     * LLM Domain 연결 상태를 확인합니다.
     *
     * @return 연결 상태
     */
    public boolean checkConnection() {
        try {
            String healthUrl = llmApiUrl.replace("/recommend", "/health");
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.warn("LLM Domain 연결 확인 실패: {}", e.getMessage());
            return false;
        }
    }
}
