FROM ubuntu:latest
LABEL authors="john.maicon"

RUN apt-get update && apt-get install -y procps
RUN apt-get install openjdk-21-jre -y
RUN . .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:21-jre-slim

EXPOSE 8080

COPY --from=build /app/target/m7-ch-platform-server-*-exec.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]