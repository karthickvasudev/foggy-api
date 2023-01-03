FROM openjdk:18-alpine
EXPOSE 8080
CMD mvn clean install
ADD target/*.jar foggy-api.jar
ENTRYPOINT ["java","-jar","/foggy-api.jar"]