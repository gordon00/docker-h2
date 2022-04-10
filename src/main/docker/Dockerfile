FROM openjdk:8-jre-alpine

ARG H2_VERSION

RUN apk add --no-cache curl && \
    HTTP_CODE=$(curl https://repo1.maven.org/maven2/com/h2database/h2/${H2_VERSION}/h2-${H2_VERSION}.jar -o h2.jar --silent --write-out "%{http_code}") && \
    [[ ${HTTP_CODE} = 200 ]] && \
    apk del curl
WORKDIR /
ENTRYPOINT java $JAVA_OPTS -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -cp h2.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -ifNotExists -baseDir /h2-data
HEALTHCHECK CMD java -cp h2.jar org.h2.tools.Shell -url jdbc:h2:tcp://localhost:9092/heartbeat -sql "select 1;"
EXPOSE 8082 9092

VOLUME /h2-data
