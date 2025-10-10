package com.youthfi.finance.global.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 내부 요청 식별 필터
 * XUserAuthenticationFilter보다 먼저 실행됨. 
 */
@Slf4j
@Component
@Order(1)
public class InternalRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                        FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String remoteAddr = httpRequest.getRemoteAddr();
        
        boolean isInternal = isInternalRequest(remoteAddr);
        
        if (isInternal) {
            log.debug("내부 요청 감지: IP={}, URI={}", remoteAddr, httpRequest.getRequestURI());
            httpRequest.setAttribute("isInternalRequest", true);
        } else {
            log.debug("외부 요청 감지: IP={}, URI={}", remoteAddr, httpRequest.getRequestURI());
            httpRequest.setAttribute("isInternalRequest", false);
        }
        
        chain.doFilter(request, response);
    }
    
    private boolean isInternalRequest(String remoteAddr) {
        if (remoteAddr == null) {
            return false;
        }
        
        return remoteAddr.startsWith("10.")
            || remoteAddr.startsWith("192.168.")
            || remoteAddr.equals("127.0.0.1")
            || remoteAddr.equals("0:0:0:0:0:0:0:1"); 
    }
}
