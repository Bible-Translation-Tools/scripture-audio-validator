FROM gradle:8-jdk11 AS builder
COPY . /sav-server
WORKDIR /sav-server
RUN gradle build web:shadowjar

FROM openjdk:11-jre-slim

RUN mkdir /app

WORKDIR /app
COPY --from=builder /sav-server/web/build/libs/scripture-audio-validator_web.jar sav.jar
ENV UPLOAD_DIR=/upload
EXPOSE 8080

CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "sav.jar"]
