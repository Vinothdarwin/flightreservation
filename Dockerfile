FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD https://staticdownloads.site24x7.com/apminsight/agents/apminsight-javaagent.zip /apminsight-javaagent.zip
RUN ["unzip","-d", "/", "/apminsight-javaagent.zip"]
ARG JAR_FILE
COPY ${JAR_FILE} /app.jar
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["sh","entrypoint.sh"]