package com.youthfi.finance.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.youthfi.finance.global.config.properties.AiApiProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AiRestTemplateConfig {

    private final AiApiProperties aiApiProperties;

    @Bean("aiRestTemplate")
    public RestTemplate aiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(createRequestFactory());
        return restTemplate;
    }

    private ClientHttpRequestFactory createRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        
        // 챗봇 API 타임아웃 설정
        int timeout = Math.max(aiApiProperties.getChatbot().getTimeout(), 
                              aiApiProperties.getPortfolio().getTimeout());
        
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        
        return factory;
    }
}
