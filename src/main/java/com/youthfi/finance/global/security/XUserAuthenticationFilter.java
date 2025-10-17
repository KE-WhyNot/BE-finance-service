package com.youthfi.finance.global.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.youthfi.finance.global.exception.GlobalException;
import com.youthfi.finance.global.exception.UserException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class XUserAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // 1. X-User-Id 헤더에서 사용자 ID 추출
            String userId = request.getHeader("X-User-Id");
            log.debug("X-User-Id 헤더 값: {}", userId);
            
            if (StringUtils.hasText(userId)) {
                // 2. 사용자 ID 유효성 검증 (간단한 형식 체크)
                boolean isValid = isValidUserId(userId);
                log.debug("사용자 ID 유효성 검증 결과: {} for userId: {}", isValid, userId);
                
                if (isValid) {
                    // 3. SecurityContext에 인증 정보 설정
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    log.debug("X-User-Id 인증 성공: {}", userId);
                } else {
                    log.warn("유효하지 않은 사용자 ID 형식: {}", userId);
                    UserException.validateUserId(userId); // 예외 발생
                }
            } else {
                log.warn("X-User-Id 헤더가 없습니다. 요청 URI: {}", request.getRequestURI());
                throw UserException.invalidUserId("X-User-Id 헤더가 필요합니다.");
            }
        } catch (Exception e) {
            log.error("X-User-Id 인증 처리 중 오류 발생: {}", e.getMessage(), e);
            throw GlobalException.authenticationProcessingError(e);
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * 사용자 ID 유효성 검증
     * - 숫자로만 구성된 문자열인지 확인
     * - 빈 문자열이 아닌지 확인
     */
    private boolean isValidUserId(String userId) {
        return StringUtils.hasText(userId) && userId.matches("^[a-zA-Z0-9_-]+$");
    }



    /**
     * 특정 경로는 인증을 건너뛰도록 설정
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        log.debug("XUserAuthenticationFilter shouldNotFilter 체크: {}", path);
        
        // 공개 엔드포인트는 인증 건너뛰기
        boolean shouldSkip =
               path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/api-docs") ||
               path.startsWith("/h2-console") ||
               path.startsWith("/actuator") ||
               path.equals("/api/stock/token-status") ||
               path.equals("/api/stock/current-price") ||
               path.startsWith("/api/stock/chart/") ||
               path.contains("swagger") ||
               path.contains("api-docs") ||
               path.equals("/api-docs/swagger-config");
        
        if (shouldSkip) {
            log.debug("인증 필터 건너뛰기: {}", path);
        } else {
            log.debug("인증 필터 적용: {}", path);
        }
        
        return shouldSkip;
    }
}
