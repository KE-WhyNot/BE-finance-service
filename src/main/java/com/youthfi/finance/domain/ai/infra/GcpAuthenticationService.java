package com.youthfi.finance.domain.ai.infra;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.IdToken;
import com.google.auth.oauth2.IdTokenProvider;
import com.youthfi.finance.global.config.properties.AiApiProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GcpAuthenticationService {

    private final AiApiProperties aiApiProperties;

    /**
     * GCP 서비스 계정 키를 사용하여 ID 토큰 발급
     */
    public String generateIdToken() {
        try {
            GoogleCredentials credentials;
            String key = aiApiProperties.getGcp().getServiceAccount().getKey();

            if (key != null && !key.isBlank()) {
                // JSON 파일 경로인지 JSON 문자열인지 확인
                if (key.startsWith("classpath:") || key.startsWith("file:") || key.endsWith(".json")) {
                    // 파일 경로인 경우
                    InputStream keyStream = getKeyStream(key);
                    credentials = GoogleCredentials.fromStream(keyStream);
                } else {
                    // JSON 문자열인 경우
                    credentials = GoogleCredentials.fromStream(
                            new ByteArrayInputStream(key.getBytes(StandardCharsets.UTF_8))
                    );
                }
            } else {
                // 키 미설정 시 ADC (Application Default Credentials) 사용
                credentials = GoogleCredentials.getApplicationDefault();
            }

            if (credentials instanceof IdTokenProvider idTokenProvider) {
                String targetAudience = aiApiProperties.getGcp().getTargetAudience();
                IdToken idToken = idTokenProvider.idTokenWithAudience(targetAudience, null);
                log.info("GCP ID 토큰 발급 성공");
                return idToken.getTokenValue();
            } else {
                log.error("현재 Credentials 타입은 ID 토큰 발급을 지원하지 않습니다: {}", credentials.getClass().getName());
                throw new RuntimeException("ID 토큰 발급을 지원하지 않는 인증 방식입니다.");
            }

        } catch (IOException e) {
            log.error("GCP ID 토큰 발급 실패: {}", e.getMessage(), e);
            throw new RuntimeException("GCP ID 토큰 발급 실패", e);
        }
    }

    /**
     * 키 스트림 가져오기 (파일 경로 처리)
     */
    private InputStream getKeyStream(String keyPath) throws IOException {
        if (keyPath.startsWith("classpath:")) {
            // classpath 리소스
            String resourcePath = keyPath.substring("classpath:".length());
            ClassPathResource resource = new ClassPathResource(resourcePath);
            return resource.getInputStream();
        } else if (keyPath.startsWith("file:")) {
            // 파일 시스템 경로
            String filePath = keyPath.substring("file:".length());
            return new FileInputStream(filePath);
        } else if (keyPath.endsWith(".json")) {
            // 상대 경로인 경우 classpath에서 찾기
            ClassPathResource resource = new ClassPathResource(keyPath);
            return resource.getInputStream();
        } else {
            // 절대 경로인 경우
            return new FileInputStream(keyPath);
        }
    }

    /**
     * 인증 헤더 생성
     */
    public String getAuthorizationHeader() {
        String idToken = generateIdToken();
        return "Bearer " + idToken;
    }

    /**
     * Cloud Run 서비스 인증을 위한 헤더 생성
     */
    public String getCloudRunAuthHeader() {
        return getAuthorizationHeader();
    }
}
