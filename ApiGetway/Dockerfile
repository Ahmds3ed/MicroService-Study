FROM openjdk:17
#create image from this jdk

COPY target/*.jar app.jar
#copy the jar file from target to container

ENTRYPOINT ["java","-jar","/app.jar"]

#Entry point mean after the containr start will run this >>  java -jar /app.jar