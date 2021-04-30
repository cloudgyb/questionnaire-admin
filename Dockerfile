FROM openjdk:11

EXPOSE 80

ADD target/*.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
