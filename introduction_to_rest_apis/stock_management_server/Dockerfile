# Note - alpine doesn't work on arm chip architectures.
# Note - our use case is a bit inverted from the norm - we care mostly about build times and portability, and less about end image size.
# Multi-stage build doesn't give us anything, because we don't care about the final image size.
FROM eclipse-temurin:17-jdk
USER root
WORKDIR /app

# Get gradle distribution
COPY *.gradle.kts gradle.* gradlew ./
COPY gradle gradle

# Cuts ~20s off a cached build
RUN ./gradlew --version

# Cuts ~50s off a cached build - this will no longer help within a session, because people won't be rebuilding, but may help between sessions, depending on the amount of shared code.
RUN ./gradlew build --stacktrace

COPY ./src ./src
RUN ./gradlew build --stacktrace

EXPOSE 7070

# Run will also recompile the server, so after people make changes, they just need to restart the container.
# All cached stuff will be in the writable container layer.
# Separate compile and run container plan may help with compile time on restart though?
# "docker compose restart dev-server" not logging build errors.
CMD ["./gradlew", "run"]
