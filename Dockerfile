# Stage 1: Build stage using JDK 23
FROM eclipse-temurin:23 AS builder

USER root
RUN apt-get update && apt-get install -y wget unzip && rm -rf /var/lib/apt/lists/*

ENV GRADLE_VERSION=8.2.1

RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip && \
    unzip gradle-${GRADLE_VERSION}-bin.zip -d /opt && \
    rm gradle-${GRADLE_VERSION}-bin.zip

ENV GRADLE_HOME=/opt/gradle-${GRADLE_VERSION}
ENV PATH=${GRADLE_HOME}/bin:${PATH}

WORKDIR /home/gradle/project

COPY . .

RUN chmod +x gradlew && ./gradlew bootJar --no-daemon --stacktrace --info


FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
