FROM openjdk:17
COPY target/taskmesh-api-0.0.1-SNAPSHOT.jar taskmesh.jar
CMD ["java", "-jar", "taskmesh.jar"]
