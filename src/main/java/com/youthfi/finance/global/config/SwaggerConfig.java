package com.youthfi.finance.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Youth-Fi Finance Service API")
                        .description("카카오엔터프라이즈 Youth-Fi 금융 서비스 백엔드 API<br/>" +
                                "인증은 별도의 Auth Service와 nginx ingress controller를 통해 처리됩니다.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Youth-Fi Team")
                                .email("youthfi@kakaoenterprise.com")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                );
    }
}