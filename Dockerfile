FROM gradle:8.6.0-jdk21-alpine AS builder
WORKDIR /app
COPY . .
RUN gradle build

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]