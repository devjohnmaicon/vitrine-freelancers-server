# Estágio de build
FROM maven:3.9.6-eclipse-temurin-21 AS build
LABEL authors="john.maicon"

# Copia TODOS os arquivos do projeto
COPY . .

# Compila o projeto com encoding UTF-8
RUN mvn clean package -DskipTests

# Estágio de produção
FROM eclipse-temurin:21-jre

WORKDIR /app

EXPOSE 8080

# Copia o JAR do estágio de build
COPY --from=build /target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]