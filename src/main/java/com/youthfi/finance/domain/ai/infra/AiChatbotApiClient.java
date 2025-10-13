package com.youthfi.finance.domain.ai.infra;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.youthfi.finance.domain.ai.application.dto.request.ChatRequest;
import com.youthfi.finance.domain.ai.application.dto.response.ChatResponse;
import com.youthfi.finance.global.config.properties.AiApiProperties;
import com.youthfi.finance.global.exception.AiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AiChatbotApiClient {

    @Qualifier("restTemplate")
    private final RestTemplate restTemplate;
    private final GcpAuthenticationService gcpAuthenticationService;

    private final AiApiProperties aiApiProperties;

    /**
     * AI 챗봇과 채팅 요청
     */
    public ChatResponse sendChatRequest(ChatRequest request) {
        try {
            log.info("AI 챗봇 API 호출 시작: userId={}, sessionId={}", request.user_id(), request.session_id());

            // 요청 헤더 설정 (GCP 인증 포함)
            HttpHeaders headers = createAuthHeaders();
            
            // HTTP 엔티티 생성
            HttpEntity<ChatRequest> httpEntity = new HttpEntity<>(request, headers);
            
            // API 호출
            String apiUrl = aiApiProperties.getChatbot().getApiUrl();
            log.info("AI 챗봇 API URL: {}", apiUrl);
            
            if (apiUrl == null || apiUrl.trim().isEmpty()) {
                throw new RuntimeException("AI 챗봇 API URL이 설정되지 않았습니다.");
            }
            
            String fullUrl = apiUrl + "/api/v1/chat";
            log.info("전체 API URL: {}", fullUrl);
            
            ResponseEntity<ChatResponse> response = restTemplate.exchange(
                    fullUrl,
                    HttpMethod.POST,
                    httpEntity,
                    ChatResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("AI 챗봇 API 호출 성공: userId={}", request.user_id());
                return response.getBody();
            } else {
                log.error("AI 챗봇 API 호출 실패: status={}, userId={}", 
                         response.getStatusCode(), request.user_id());
                throw AiException.chatbotApiConnectionFailed(
                    new RuntimeException("AI 챗봇 API 호출 실패: " + response.getStatusCode()));
            }

        } catch (Exception e) {
            log.error("AI 챗봇 API 호출 중 오류 발생: userId={}, error={}", 
                     request.user_id(), e.getMessage(), e);
            throw AiException.chatbotApiConnectionFailed(e);
        }
    }

    /**
     * 대화 기록 조회
     */
    public Map<String, Object> getConversationHistory(String sessionId) {
        try {
            log.info("대화 기록 조회 시작: sessionId={}", sessionId);

            HttpHeaders headers = createAuthHeaders();
            HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                    aiApiProperties.getChatbot().getApiUrl() + "/api/v1/chat/history/" + sessionId,
                    HttpMethod.GET,
                    httpEntity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("대화 기록 조회 성공: sessionId={}", sessionId);
                return response.getBody();
            } else {
                log.error("대화 기록 조회 실패: status={}, sessionId={}", 
                         response.getStatusCode(), sessionId);
                throw AiException.chatbotApiConnectionFailed(
                    new RuntimeException("대화 기록 조회 실패: " + response.getStatusCode()));
            }

        } catch (Exception e) {
            log.error("대화 기록 조회 중 오류 발생: sessionId={}, error={}", 
                     sessionId, e.getMessage(), e);
            throw AiException.chatbotApiConnectionFailed(e);
        }
    }

    /**
     * 대화 기록 초기화
     */
    public Map<String, Object> clearConversationHistory(String sessionId) {
        try {
            log.info("대화 기록 초기화 시작: sessionId={}", sessionId);

            HttpHeaders headers = createAuthHeaders();
            HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                    aiApiProperties.getChatbot().getApiUrl() + "/api/v1/chat/history/" + sessionId,
                    HttpMethod.DELETE,
                    httpEntity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("대화 기록 초기화 성공: sessionId={}", sessionId);
                return response.getBody();
            } else {
                log.error("대화 기록 초기화 실패: status={}, sessionId={}", 
                         response.getStatusCode(), sessionId);
                throw AiException.chatbotApiConnectionFailed(
                    new RuntimeException("대화 기록 초기화 실패: " + response.getStatusCode()));
            }

        } catch (Exception e) {
            log.error("대화 기록 초기화 중 오류 발생: sessionId={}, error={}", 
                     sessionId, e.getMessage(), e);
            throw AiException.chatbotApiConnectionFailed(e);
        }
    }

    /**
     * 성능 메트릭 조회
     */
    public Map<String, Object> getPerformanceMetrics() {
        try {
            log.info("성능 메트릭 조회 시작");

            HttpHeaders headers = createAuthHeaders();
            HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                    aiApiProperties.getChatbot().getApiUrl() + "/api/v1/chat/metrics",
                    HttpMethod.GET,
                    httpEntity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("성능 메트릭 조회 성공");
                return response.getBody();
            } else {
                log.error("성능 메트릭 조회 실패: status={}", response.getStatusCode());
                throw AiException.chatbotApiConnectionFailed(
                    new RuntimeException("성능 메트릭 조회 실패: " + response.getStatusCode()));
            }

        } catch (Exception e) {
            log.error("성능 메트릭 조회 중 오류 발생: error={}", e.getMessage(), e);
            throw AiException.chatbotApiConnectionFailed(e);
        }
    }

    /**
     * 지식 베이스 통계 조회
     */
    public Map<String, Object> getKnowledgeBaseStats() {
        try {
            log.info("지식 베이스 통계 조회 시작");

            HttpHeaders headers = createAuthHeaders();
            HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                    aiApiProperties.getChatbot().getApiUrl() + "/api/v1/chat/knowledge-base/stats",
                    HttpMethod.GET,
                    httpEntity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("지식 베이스 통계 조회 성공");
                return response.getBody();
            } else {
                log.error("지식 베이스 통계 조회 실패: status={}", response.getStatusCode());
                throw AiException.chatbotApiConnectionFailed(
                    new RuntimeException("지식 베이스 통계 조회 실패: " + response.getStatusCode()));
            }

        } catch (Exception e) {
            log.error("지식 베이스 통계 조회 중 오류 발생: error={}", e.getMessage(), e);
            throw AiException.chatbotApiConnectionFailed(e);
        }
    }

    /**
     * AI 챗봇 서비스 연결 상태 확인
     */
    public boolean checkConnection() {
        try {
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    aiApiProperties.getChatbot().getApiUrl() + "/health",
                    HttpMethod.GET,
                    httpEntity,
                    String.class
            );
            
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.warn("AI 챗봇 서비스 연결 확인 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 인증 헤더 생성
     */
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", gcpAuthenticationService.getCloudRunAuthHeader());
        headers.set("X-Request-Source", "finance-service");
        headers.set("X-Request-Type", "ai-chatbot");
        return headers;
    }
}
