FROM openjdk:8
EXPOSE 8080
ADD target/gaming-backend-0.0.1-SNAPSHOT.war gaming-backend-0.0.1-SNAPSHOT.war
ENTRYPOINT ["java","-jar","/gaming-backend-0.0.1-SNAPSHOT.war"]
