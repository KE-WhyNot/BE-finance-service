package com.youthfi.finance.global.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.youthfi.finance.global.config.properties.CorsProperties;
import com.youthfi.finance.global.security.XUserAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final XUserAuthenticationFilter xUserAuthenticationFilter;
    private final CorsProperties corsProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 비활성화 (JWT 사용시 불필요)
            .csrf(AbstractHttpConfigurer::disable)
            
            // CORS 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 세션 비활성화 (JWT 사용)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 요청별 권한 설정
            .authorizeHttpRequests(authz -> authz
                // 공개 엔드포인트
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/h2-console/**",
                    "/actuator/**",
                    "/api/stock/token-status",
                    "/api/stock/current-price",
                    "/api/stock/chart/**"
                ).permitAll()
                
                // 인증이 필요한 엔드포인트
                .requestMatchers(
                    "/api/user/**",
                    "/api/stock/trading/**",
                    "/api/stock/user-stock/**",
                    "/api/portfolio/**",
                    "/api/ai/**"
                ).authenticated()
        
                // 기타 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            
            // X-User-Id 인증 필터 추가
            .addFilterBefore(xUserAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // CorsProperties에서 설정값 가져오기
        configuration.setAllowedOriginPatterns(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        configuration.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        configuration.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(corsProperties.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
