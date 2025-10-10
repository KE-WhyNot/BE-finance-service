package com.youthfi.finance.global.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SecurityUtils {
    
    /**
     * 현재 인증된 사용자 ID 조회
     */
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        
        throw new RuntimeException("인증된 사용자를 찾을 수 없습니다.");
    }
    
    /**
     * Request에서 X-User-Id 헤더 조회
     */
    public static String getUserIdFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String userId = (String) request.getAttribute("X-User-Id");
            
            if (userId != null) {
                return userId;
            }
        }
        
        throw new RuntimeException("사용자 ID를 찾을 수 없습니다.");
    }
    
    /**
     * 현재 사용자가 요청한 사용자와 일치하는지 확인
     */
    public static void validateUserAccess(String requestedUserId) {
        String currentUserId = getCurrentUserId();
        
        if (!currentUserId.equals(requestedUserId)) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }
    }
}
