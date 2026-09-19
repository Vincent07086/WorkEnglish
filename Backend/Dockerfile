FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN apt-get update && apt-get install -y maven && mvn -q -DskipTests package
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/englishlog-api-1.0.0.jar app.jar
EXPOSE 5050
ENTRYPOINT ["java","-jar","app.jar"]
