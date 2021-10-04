FROM amazoncorretto:11.0.4
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} /tmp/assessment_1.jar
ENTRYPOINT ["java","-jar","/tmp/assessment_1.jar"]
