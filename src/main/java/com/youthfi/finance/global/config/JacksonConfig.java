package com.youthfi.finance.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Jackson JSON 직렬화 설정
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // JSR310 모듈 등록 (Java 8 Time API 지원)
        mapper.registerModule(new JavaTimeModule());
        
        // 날짜를 타임스탬프가 아닌 ISO 문자열로 직렬화
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // 알 수 없는 속성 무시
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        return mapper;
    }
}
