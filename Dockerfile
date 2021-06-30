# base image
#FROM adoptopenjdk/openjdk11:alpine-jre
# we will use openjdk 8 with alpine as it is a very small linux distro
FROM openjdk:8-jre-alpine3.9
# home folder in the Docker image
RUN mkdir -p /software

# copy config files (application.properties)
#ADD config /software/config
# copy jar file
ADD target/myapp.jar /software/myapp.jar

# run the app
WORKDIR /software
CMD java -Dserver.port=$PORT $JAVA_OPTS -jar myapp.jar
