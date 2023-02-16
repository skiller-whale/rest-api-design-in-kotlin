FROM eclipse-temurin:17-jdk
USER root
WORKDIR /app

COPY *.gradle.kts gradle.* gradlew ./
COPY gradle gradle

RUN ./gradlew --version

RUN ./gradlew build --stacktrace

COPY ./src ./src
RUN ./gradlew build --stacktrace

EXPOSE 7070

CMD ["./gradlew", "run"]
