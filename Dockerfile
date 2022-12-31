FROM amd64/gradle:7.4-jdk-alpine
WORKDIR /app
ADD build.gradle /app/

ENV AWS_DEV_DB_URL=jdbc:mysql://eroom-db.cpghnwiewsdn.ap-northeast-2.rds.amazonaws.com:3306/PROJECT_DEV \
    AWS_PROD_DB_URL=jdbc:mysql://eroom-db.cpghnwiewsdn.ap-northeast-2.rds.amazonaws.com:3306/PROJECT_PROD \
    AWS_TEST_DB_URL=jdbc:mysql://eroom-db.cpghnwiewsdn.ap-northeast-2.rds.amazonaws.com:3306/PROJECT_TEST \
    AWS_PASSWORD=qlalfqjsgh1! \
    AWS_USERNAME=admin \
    AWS_ACCESS_KEY_ID=AKIA2TUNUHCY6ACXD4VA \
    AWS_SECRET_ACCESS_KEY=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 \
    ACTIVE_PROFILES=dev
RUN mkdir /thumbnail
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

COPY . /app
RUN gradle clean build --exclude-task test --no-daemon
CMD java -jar build/libs/*.jar
