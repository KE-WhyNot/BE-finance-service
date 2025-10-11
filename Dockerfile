# ===========================================
# Youth-Fi Finance Service Dockerfile
# Spring Boot 3.5.5 + JDK 17 + Multi-stage build
# ===========================================

# Stage 1: Build stage
FROM gradle:8.5-jdk17 AS builder

# 작업 디렉토리 설정
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
FROM openjdk:17-jre-slim

# GCP 서비스 계정 키를 빌드 인수로 받음
ARG GCP_SA_KEY

# 메타데이터 라벨
LABEL maintainer="youthfi-team"
LABEL description="Youth-Fi Finance Service - Spring Boot 3.5.5"
LABEL version="1.0.0"

# 작업 디렉토리 설정
WORKDIR /app

# 시스템 패키지 업데이트 및 필요한 도구 설치
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/*

# 애플리케이션 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# GCP 서비스 계정 키를 위한 디렉토리 생성
RUN mkdir -p /app/config

# GCP 서비스 계정 키 파일 생성 (빌드 시점에)
RUN if [ -n "$GCP_SA_KEY" ]; then \
        echo "$GCP_SA_KEY" > /app/config/gcp-service-account.json; \
    fi

# 환경변수 설정
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseContainerSupport"
ENV GCP_SA_KEY_FILE=/app/config/gcp-service-account.json

# 포트 노출
EXPOSE 8082

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
