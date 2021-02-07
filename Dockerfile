FROM openjdk:11

EXPOSE 8081

WORKDIR /applications

COPY target/GEA_API-1.0-SNAPSHOT.jar /applications/application.jar

ENTRYPOINT ["java","-jar", "application.jar"]