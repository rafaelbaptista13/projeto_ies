FROM openjdk:11
EXPOSE 8080
WORKDIR /app
COPY Covinfo19/target/Covinfo19-0.0.1-SNAPSHOT.jar .
ENTRYPOINT [ "java", "-jar", "Covinfo19-0.0.1-SNAPSHOT.jar" ]
