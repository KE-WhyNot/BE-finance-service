package com.youthfi.finance.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Youth-Fi Finance Service API")
                        .description("카카오엔터프라이즈 Youth-Fi 금융 서비스 백엔드 API<br/>" +
                                "인증은 X-User-Id 헤더를 통해 처리됩니다.<br/>" +
                                "Swagger UI에서 테스트할 때는 X-User-Id 헤더에 사용자 ID를 입력해주세요.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Youth-Fi Team")
                                .email("youthfi@kakaoenterprise.com")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                )
                .addServersItem(new Server().url("http://localhost:8081").description("Local Development Server"))
                .addServersItem(new Server().url("https://finance.youth-fi.com").description("HTTPS Server"))
                .components(new Components()
                        .addSecuritySchemes("X-User-Id", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-User-Id")
                                .description("사용자 인증을 위한 사용자 ID 헤더")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("X-User-Id"));
    }
}