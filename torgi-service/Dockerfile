# Stage 1: Build stage
FROM openjdk:17-slim AS build

# Установка Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml .
RUN mvn -N install

COPY /torgi-service/. .

RUN mvn install -DskipTests

# Stage 2: Runtime stage
FROM openjdk:17-slim

# Установка сертификатов и необходимых инструментов
RUN apt-get update && apt-get install -y ca-certificates curl && apt-get clean

# Скачивание и установка сертификатов
RUN curl -o /tmp/russian_trusted_root_ca_pem.crt https://gu-st.ru/content/lending/russian_trusted_root_ca_pem.crt \
    && curl -o /tmp/russian_trusted_sub_ca_pem.crt https://gu-st.ru/content/lending/russian_trusted_sub_ca_pem.crt \
    && keytool -import -trustcacerts -file /tmp/russian_trusted_root_ca_pem.crt -alias russian_root -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt \
    && keytool -import -trustcacerts -file /tmp/russian_trusted_sub_ca_pem.crt -alias russian_sub -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt \
    && rm /tmp/russian_trusted_root_ca_pem.crt /tmp/russian_trusted_sub_ca_pem.crt

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
