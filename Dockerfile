FROM openjdk:11
ENV AWS_DEV_DB_URL=jdbc:mysql://project.c5ukj6j3krdj.us-west-1.rds.amazonaws.com:3306/PROJECT_DEV \
    AWS_PROD_DB_URL=jdbc:mysql://project.c5ukj6j3krdj.us-west-1.rds.amazonaws.com:3306/PROJECT_PROD \
    AWS_TEST_DB_URL=jdbc:mysql://project.c5ukj6j3krdj.us-west-1.rds.amazonaws.com:3306/PROJECT_TEST \
    AWS_PASSWORD=qlalfqjsgh1! \
    AWS_USERNAME=admin

ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]