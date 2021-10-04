FROM amazoncorretto:11.0.4
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} assessment_1.jar
ENTRYPOINT ["java","-jar","/assessment_1.jar"]
