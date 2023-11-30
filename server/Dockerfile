FROM openjdk:18-jdk-alpine3.14
RUN mkdir /app
COPY ./build/libs/server-all.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]