FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw && ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /build/target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
