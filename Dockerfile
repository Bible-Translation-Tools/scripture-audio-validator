FROM gradle:8-jdk11 AS builder
COPY . /sav-server
WORKDIR /sav-server
RUN gradle build

FROM openjdk:11-jre-slim

RUN mkdir /app

COPY --from=builder /sav-server/build/libs/scripture-audio-validator_web.jar.jar /app/scripture-audio-validator.jar
WORKDIR /app

CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "scripture-audio-validator.jar"]
