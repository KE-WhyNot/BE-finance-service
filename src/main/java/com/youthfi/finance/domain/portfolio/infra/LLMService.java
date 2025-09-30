package com.youthfi.finance.domain.portfolio.infra;

import com.youthfi.finance.domain.portfolio.domain.entity.InvestmentProfile;
import com.youthfi.finance.domain.user.domain.entity.User;
import com.youthfi.finance.domain.stock.domain.repository.StockRepository;
import com.youthfi.finance.domain.stock.domain.repository.SectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LLMService {

    private final RestTemplate restTemplate;
    private final StockRepository stockRepository;
    private final SectorRepository sectorRepository;

    @Value("${llm.api.url:http://localhost:8081/api/llm/recommend}")
    private String llmApiUrl;
    
    @Value("${llm.api.key:your-api-key}")
    private String llmApiKey;
    
    @Value("${llm.api.timeout:30000}")
    private int timeout;
    
    @Value("${llm.api.retry.max-attempts:3}")
    private int maxRetryAttempts;
    
    @Value("${llm.api.retry.delay:1000}")
    private long retryDelay;

    /**
     * 투자성향과 사용자 정보를 기반으로 AI LLM 추천 포트폴리오를 요청하고 결과를 반환합니다.
     * 예외 발생 시 기본 추천 결과를 반환합니다.
     *
     * @param investmentProfile 투자성향 정보
     * @param user 사용자 엔터티
     * @return LLM 추천 결과 맵
     */

    public Map<String, Object> recommendPortfolio(InvestmentProfile investmentProfile, User user) {
        try {
            Map<String, Object> requestData = buildLLMRequest(investmentProfile, user);
            Map<String, Object> llmResponse = callLLMApi(requestData);
            return parseLLMResponse(llmResponse);
        } catch (Exception e) {
            System.err.println("LLM 담당자 API 호출 실패: " + e.getMessage());
            e.printStackTrace();
            return getDefaultRecommendation(investmentProfile);
        }
    }

    /**
     * LLM API 호출에 필요한 요청 데이터를 만듭니다.
     *
     * @param profile 투자성향 프로필
     * @param user 사용자 엔터티
     * @return API 호출용 요청 데이터 맵
     */

    private Map<String, Object> buildLLMRequest(InvestmentProfile profile, User user) {
        Map<String, Object> request = new HashMap<>();
        request.put("userId", user.getUserId());
        request.put("userName", user.getName());
        request.put("seedMoney", user.getSeedMoney());
        request.put("currentBalance", user.getBalance());

        Map<String, Object> investmentProfile = new HashMap<>();
        investmentProfile.put("profileType", profile.getInvestmentProfile().name());
        investmentProfile.put("profileDescription", profile.getInvestmentProfile().getDescription());
        investmentProfile.put("availableAssets", profile.getAvailableAssets());
        investmentProfile.put("investmentGoal", profile.getInvestmentGoal().name());
        investmentProfile.put("goalDescription", profile.getInvestmentGoal().getDescription());
        request.put("investmentProfile", investmentProfile);

        request.put("interestedSectors", getInterestedSectors(profile.getProfileId()));
        request.put("marketData", getMarketData());
        request.put("requestId", "req_" + System.currentTimeMillis());
        request.put("timestamp", System.currentTimeMillis());
        return request;
    }

     /**
     * LLM API를 호출하여 추천 결과를 받아옵니다.
     * 최대 재시도 횟수만큼 실패 시 재시도하며, 최종 실패 시 예외를 던집니다.
     *
     * @param requestData API 요청 데이터
     * @return LLM API 응답 데이터 맵
     * @throws RuntimeException 호출 실패 시
     */

    private Map<String, Object> callLLMApi(Map<String, Object> requestData) {
        Exception lastException = null;
        for (int attempt = 1; attempt <= maxRetryAttempts; attempt++) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + llmApiKey);
                headers.set("X-Request-Source", "finance-service");
                headers.set("X-Retry-Attempt", String.valueOf(attempt));

                HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestData, headers);
                ResponseEntity<Map> response = restTemplate.exchange(
                        llmApiUrl,
                        HttpMethod.POST,
                        request,
                        Map.class
                );
                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    return response.getBody();
                } else {
                    throw new RuntimeException("LLM 담당자 API 호출 실패: " + response.getStatusCode());
                }
            } catch (Exception e) {
                lastException = e;
                if (attempt < maxRetryAttempts) {
                    try { Thread.sleep(retryDelay); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); throw new RuntimeException("재시도 대기 중 인터럽트", ie); }
                }
            }
        }
        throw new RuntimeException("LLM 담당자와의 통신 최종 실패", lastException);
    }

    /**
     * LLM API에서 반환된 응답을 파싱하여 필요한 항목만 골라 반환합니다.
     *
     * @param llmResponse LLM API 응답 데이터
     * @return 파싱된 추천 결과 맵
     */

    private Map<String, Object> parseLLMResponse(Map<String, Object> llmResponse) {
        Map<String, Object> result = new HashMap<>();
        result.put("portfolioName", llmResponse.get("portfolioName"));
        result.put("allocationStocks", llmResponse.get("allocationStocks"));
        result.put("allocationSavings", llmResponse.get("allocationSavings"));
        result.put("expected1YrReturn", llmResponse.get("expected1YrReturn"));
        result.put("recommendedStocks", llmResponse.get("recommendedStocks"));
        if (llmResponse.containsKey("reasoning")) { result.put("reasoning", llmResponse.get("reasoning")); }
        if (llmResponse.containsKey("riskLevel")) { result.put("riskLevel", llmResponse.get("riskLevel")); }
        if (llmResponse.containsKey("confidence")) { result.put("confidence", llmResponse.get("confidence")); }
        return result;
    }

    /**
     * 시장 데이터(주요 종목 및 섹터 리스트)를 조회합니다.
     *
     * @return 시장 데이터 맵
     */

    private Map<String, Object> getMarketData() {
        Map<String, Object> marketData = new HashMap<>();
        marketData.put("majorStocks", stockRepository.findAll());
        marketData.put("sectors", sectorRepository.findAll());
        return marketData;
    }

    /**
     * 투자성향 프로필 ID를 기반으로 관심 섹터 리스트를 반환합니다.
     * 현재는 하드코딩된 예시를 반환합니다.
     *
     * @param profileId 투자성향 프로필 ID
     * @return 관심 섹터명 리스트
     */

    private List<String> getInterestedSectors(Long profileId) {
        return List.of("기술", "금융", "바이오");
    }

    /**
     * LLM API 호출 실패 시 대체로 사용하는 기본 추천 포트폴리오 데이터를 만듭니다.
     *
     * @param profile 투자성향 프로필
     * @return 기본 추천 포트폴리오 맵
     */
    
    private Map<String, Object> getDefaultRecommendation(InvestmentProfile profile) {
        Map<String, Object> recommendation = new HashMap<>();
        switch (profile.getInvestmentProfile()) {
            case HIGH_RISK:
                recommendation.put("allocationStocks", BigDecimal.valueOf(70));
                recommendation.put("allocationSavings", BigDecimal.valueOf(30));
                recommendation.put("expected1YrReturn", BigDecimal.valueOf(12.0));
                break;
            case BALANCED:
                recommendation.put("allocationStocks", BigDecimal.valueOf(50));
                recommendation.put("allocationSavings", BigDecimal.valueOf(50));
                recommendation.put("expected1YrReturn", BigDecimal.valueOf(8.0));
                break;
            case CONSERVATIVE:
                recommendation.put("allocationStocks", BigDecimal.valueOf(20));
                recommendation.put("allocationSavings", BigDecimal.valueOf(80));
                recommendation.put("expected1YrReturn", BigDecimal.valueOf(4.0));
                break;
            default:
                recommendation.put("allocationStocks", BigDecimal.valueOf(40));
                recommendation.put("allocationSavings", BigDecimal.valueOf(60));
                recommendation.put("expected1YrReturn", BigDecimal.valueOf(6.0));
        }
        recommendation.put("portfolioName", "기본 추천 포트폴리오");
        recommendation.put("recommendedStocks", List.of(
                Map.of("stockId", "005930", "allocationPct", BigDecimal.valueOf(30)),
                Map.of("stockId", "000660", "allocationPct", BigDecimal.valueOf(25)),
                Map.of("stockId", "035420", "allocationPct", BigDecimal.valueOf(20)),
                Map.of("stockId", "207940", "allocationPct", BigDecimal.valueOf(15))
        ));
        return recommendation;
    }
}


