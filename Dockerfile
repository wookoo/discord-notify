FROM gradle:8.10-jdk17 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle shadowJar --no-daemon
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
ENV TZ=Asia/Seoul
COPY --from=builder /app/build/libs/*.jar /app/bot.jar
ENTRYPOINT ["java", "-jar", "/app/bot.jar"]
