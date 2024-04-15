# Para simplificar o processo, vamos usar o mesmo Dockerfile para contruir e executar. Mas o ideal seria separar em dois arquivos.
# Etapa de build usando Maven e Eclipse Temurin JDK 22
FROM maven:3.9.6-eclipse-temurin-22 AS build
WORKDIR /app
# Copiar o pom.xml e fontes
COPY pom.xml .
COPY src ./src
# Construir o aplicativo
RUN mvn clean package

# Etapa de extração para utilizar o layout de camadas
FROM eclipse-temurin:22-jdk AS extract
WORKDIR /extracted
COPY --from=build /app/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Etapa final para construir a imagem executável
FROM eclipse-temurin:22-jdk
WORKDIR /application
COPY --from=extract /extracted/dependencies/ ./
COPY --from=extract /extracted/spring-boot-loader/ ./
COPY --from=extract /extracted/snapshot-dependencies/ ./
COPY --from=extract /extracted/application/ ./

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
