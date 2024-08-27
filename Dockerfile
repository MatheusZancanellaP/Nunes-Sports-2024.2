
FROM openjdk:21-jdk-slim


RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/nunes-products-0.0.1-SNAPSHOT.jar"]

