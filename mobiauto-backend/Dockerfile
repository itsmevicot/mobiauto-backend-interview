# Usar a imagem oficial do OpenJDK 21 para compilar e rodar o projeto
FROM openjdk:21-jdk AS build
WORKDIR /app

# Instalar Maven manualmente usando curl
ARG MAVEN_VERSION=3.8.6
ARG BASE_URL=https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries

RUN curl -fsSL -o apache-maven-${MAVEN_VERSION}-bin.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/bin/mvn

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Usar a mesma imagem do OpenJDK 21 para rodar a aplicação
FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/target/mobiauto-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
