FROM maven:3.9.7-eclipse-temurin-17-alpine
WORKDIR /tests
COPY ../pom.xml .
COPY ../ .
RUN mvn test -Dtest=SeleniumRealworldappApplicationTests # Install only the dependencies needed for the tests

# Copy the certificate to the container
COPY .docker/localhost.crt /usr/local/share/ca-certificates/localhost.crt

# Add the certificate to the Java certificate store
RUN apk add --no-cache ca-certificates && \
    update-ca-certificates && \
    keytool -importcert -noprompt -trustcacerts -alias dockerhost -file /usr/local/share/ca-certificates/localhost.crt -keystore /opt/java/openjdk/lib/security/cacerts -storepass changeit

ENTRYPOINT [ "tail", "-f", "/dev/null" ]