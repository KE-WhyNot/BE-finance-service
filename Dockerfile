# ===========================================
# Youth-Fi Finance Service Dockerfile
# Spring Boot 3.5.5 + JDK 17 + Multi-stage build
# ===========================================

# Stage 1: Build stage
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

# Gradle wrapper와 build.gradle 파일 복사 (캐시 최적화)
COPY gradle/ gradle/
COPY gradlew gradlew.bat build.gradle settings.gradle ./

# 의존성 다운로드 (캐시 레이어)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src/ src/

# 애플리케이션 빌드
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Runtime stage
FROM eclipse-temurin:17-jre-jammy

# GCP 서비스 계정 키를 빌드 인수로 받음
ARG GCP_SA_KEY

WORKDIR /app

# 보안을 위한 non-root 사용자 생성
RUN useradd -u 10001 -r -s /usr/sbin/nologin app

# JAR 파일을 위한 ARG 설정
ARG JAR_FILE=build/libs/*.jar

# 애플리케이션 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# GCP 서비스 계정 키를 위한 디렉토리 생성 및 권한 설정
RUN mkdir -p /app/config && \
    chown -R app:app /app

# GCP 서비스 계정 키 파일 생성 (빌드 시점에)
RUN if [ -n "$GCP_SA_KEY" ]; then \
        echo "$GCP_SA_KEY" > /app/config/gcp-service-account.json && \
        chown app:app /app/config/gcp-service-account.json; \
    fi

# 환경변수 설정
ENV SPRING_PROFILES_ACTIVE=prod
ENV GCP_SA_KEY_FILE=/app/config/gcp-service-account.json

# 포트 노출
EXPOSE 8082

# non-root 사용자로 전환
USER 10001

# 애플리케이션 실행 (JVM 메모리 최적화)
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]