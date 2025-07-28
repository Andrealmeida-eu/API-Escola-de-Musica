# Imagem oficial do JRE 17 (leve)
FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY ./build/libs/*.jar app.jar


EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]