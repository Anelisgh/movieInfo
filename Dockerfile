FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY backend/pom.xml backend/mvnw* ./
COPY backend/.mvn .mvn
RUN ./mvnw dependency:go-offline -B
COPY backend/src src
COPY frontend/build src/main/resources/static/
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/movieInfo.jar .
EXPOSE 8080
ENV TZ=Europe/Bucharest
CMD ["java", "-jar", "movieInfo.jar"]