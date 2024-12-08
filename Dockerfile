FROM eclipse-temurin:21-jdk-jammy as builder

WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/searchable-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080 5005

# Base entrypoint for the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Default command if no command is passed
CMD ["app.jar"]
