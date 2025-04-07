FROM gradle:8.10-jdk17 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .

# 의존성 캐싱 → 빠른 빌드
RUN gradle build --no-daemon

# 2단계: 경량 런타임 이미지
FROM eclipse-temurin:17-jdk-alpine

ENV TZ=Asia/Seoul

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/bot.jar

ENTRYPOINT ["java", "-jar", "/app/bot.jar"]