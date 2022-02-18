FROM openjdk:11-jre-slim
COPY target/library-0.0.1-SNAPSHOT.jar /app/springboot-library.jar
ENTRYPOINT ["java", "-jar", "/app/springboot-library.jar"]
