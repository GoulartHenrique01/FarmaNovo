FROM maven:3.9.9-eclipse-temurin-25-noble AS builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre-noble

WORKDIR /app

COPY --from=builder /app/target/pharma-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar","pharma-0.0.1-SNAPSHOT.jar"]