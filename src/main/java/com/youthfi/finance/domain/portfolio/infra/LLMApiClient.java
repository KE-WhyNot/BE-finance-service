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
     * AI 서비스의 원시 응답을 Map으로 반환
     */
    public java.util.Map<String, Object> requestPortfolioRecommendation(InvestmentProfileResponse investmentProfile) {
        try {
            log.info("AI 서비스로 투자성향 프로필 전송 시작: profileId={}", investmentProfile.profileId());

            // InvestmentProfileResponse를 LLM 스펙(한글/숫자 문자열)으로 매핑
            java.util.Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("profileId", investmentProfile.profileId());
            payload.put("userId", investmentProfile.userId());
            payload.put("investmentProfile", toKoreanInvestmentProfile(investmentProfile));
            payload.put("availableAssets", investmentProfile.availableAssets());
            payload.put("lossTolerance", toNumericLossTolerance(investmentProfile));
            payload.put("financialKnowledge", toKoreanFinancialKnowledge(investmentProfile));
            payload.put("expectedProfit", toKoreanExpectedProfit(investmentProfile));
            payload.put("investmentGoal", toKoreanInvestmentGoal(investmentProfile));
            payload.put("interestedSectors", investmentProfile.interestedSectors());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", gcpAuthenticationService.getCloudRunAuthHeader());
            headers.set("X-Request-Source", "finance-service");
            headers.set("X-Request-Type", "portfolio-recommendation");

            HttpEntity<java.util.Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<java.util.Map> response = restTemplate.exchange(
                    llmApiProperties.getApi().getUrl(),
                    HttpMethod.POST,
                    request,
                    java.util.Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                java.util.Map<String, Object> aiResponse = response.getBody();
                log.info("AI 응답 성공: profileId={}, 응답={}", investmentProfile.profileId(), aiResponse);
                return aiResponse;
            } else {
                log.error("AI 응답 실패: status={}, profileId={}", response.getStatusCode(), investmentProfile.profileId());
                throw PortfolioException.llmApiConnectionFailed(new RuntimeException("AI API 호출 실패: " + response.getStatusCode()));
            }
        } catch (Exception e) {
            log.error("AI API 호출 중 오류 발생: profileId={}, error={}", investmentProfile.profileId(), e.getMessage(), e);
            throw PortfolioException.llmApiConnectionFailed(e);
        }
    }

    private String toKoreanInvestmentProfile(InvestmentProfileResponse profile) {
        if (profile.investmentProfile() == null) return null;
        return switch (profile.investmentProfile()) {
            case CONSERVATIVE -> "안정형";
            case CONSERVATIVE_SEEKING -> "안정추구형";
            case RISK_NEUTRAL -> "위험중립형";
            case AGGRESSIVE -> "적극투자형";
            case VERY_AGGRESSIVE -> "공격투자형";
        };
    }

    private String toNumericLossTolerance(InvestmentProfileResponse profile) {
        if (profile.lossTolerance() == null) return null;
        return switch (profile.lossTolerance()) {
            case NO_LOSS -> "0";
            case TEN_PERCENT -> "10";
            case THIRTY_PERCENT -> "30";
            case FIFTY_PERCENT -> "50";
            case SEVENTY_PERCENT -> "70";
            case FULL_AMOUNT -> "100";
        };
    }

    private String toKoreanFinancialKnowledge(InvestmentProfileResponse profile) {
        if (profile.financialKnowledge() == null) return null;
        return switch (profile.financialKnowledge()) {
            case VERY_LOW -> "매우 낮음";
            case LOW -> "낮음";
            case MEDIUM -> "보통";
            case HIGH -> "높음";
            case VERY_HIGH -> "매우 높음";
        };
    }

    private String toKoreanExpectedProfit(InvestmentProfileResponse profile) {
        if (profile.expectedProfit() == null) return null;
        return switch (profile.expectedProfit()) {
            case ONE_FIFTY_PERCENT -> "150";
            case TWO_HUNDRED_PERCENT -> "200";
            case TWO_FIFTY_PERCENT -> "250";
            case THREE_HUNDRED_PERCENT_PLUS -> "300 이상";
        };
    }

    private String toKoreanInvestmentGoal(InvestmentProfileResponse profile) {
        if (profile.investmentGoal() == null) return null;
        return switch (profile.investmentGoal()) {
            case EDUCATION -> "학비";
            case LIVING_EXPENSES -> "생활비";
            case HOUSE_PURCHASE -> "주택마련";
            case ASSET_GROWTH -> "자산증식";
            case DEBT_REPAYMENT -> "채무상환";
        };
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
