FROM maven:3.9-eclipse-temurin-17 AS builder

ENV SPRING_PROFILES_ACTIVE=docker

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests -pl shared -am

ARG MODULE_NAME=backend
ARG VERSION=v1
ENV MODULE_NAME=$MODULE_NAME
ENV VERSION=$VERSION

# Install shared module first
WORKDIR /app/shared
RUN mvn clean install -DskipTests

# Build the actual module
WORKDIR /app/$MODULE_NAME
RUN mvn clean package -DskipTests -Dspring.profiles.active=docker -f ./pom.xml && \
    mv target/*.jar target/survey-${MODULE_NAME}-${VERSION}.jar

FROM eclipse-temurin:17-jre

ENV SPRING_PROFILES_ACTIVE=docker

ARG MODULE_NAME=backend
ARG VERSION=v1
ENV MODULE_NAME=$MODULE_NAME
ENV VERSION=$VERSION

WORKDIR /app

COPY --from=builder /app/$MODULE_NAME/target/survey-${MODULE_NAME}-${VERSION}.jar survey-${MODULE_NAME}-${VERSION}.jar

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar survey-${MODULE_NAME}-${VERSION}.jar"]
