FROM eclipse-temurin:21-jdk

RUN mkdir -p /app/src/main/resources/static
WORKDIR /app
COPY backend/pom.xml backend/mvnw* ./
COPY backend/.mvn .mvn
COPY backend/src src
COPY --chown=1001:0 frontend/build/ src/main/resources/static/

RUN ./mvnw dependency:go-offline
RUN ./mvnw clean package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/movieInfo.jar"]