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

# 소스 코드 복사
COPY src/ src/

# 애플리케이션 빌드
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Runtime stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# 보안을 위한 non-root 사용자 생성
RUN useradd -u 10001 -r -s /usr/sbin/nologin app

# JAR 파일을 위한 ARG 설정
ARG JAR_FILE=build/libs/*.jar

# 애플리케이션 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# 파일 소유권을 app 사용자로 설정
RUN chown -R app:app /app

# 환경변수 설정
ENV SPRING_PROFILES_ACTIVE=prod

# 포트 노출
EXPOSE 8082

# non-root 사용자로 전환
USER 10001

# 애플리케이션 실행 (JVM 메모리 최적화)
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]