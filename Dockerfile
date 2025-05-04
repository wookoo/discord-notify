FROM gradle:8.10-jdk17 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle bootJar --no-daemon
FROM eclipse-temurin:17-jdk AS runtime
WORKDIR /app
ENV TZ=Asia/Seoul
COPY --from=builder /app/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=deploy", "-jar", "/app/app.jar"]