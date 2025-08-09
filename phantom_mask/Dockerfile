FROM maven:3.9.11-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn package -DskipTests -B

#------------------

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/phantom_mask-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

