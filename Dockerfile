FROM openjdk:17
EXPOSE 8888
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "./app.jar" ]
