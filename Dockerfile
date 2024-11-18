FROM eclipse-temurin:21-jre

COPY build/libs/SnippetSearcher-0.0.1-SNAPSHOT.jar java-app.jar

RUN mkdir -p /usr/local/newrelic
ADD ./newrelic/newrelic-agent-8.15.0.jar /usr/local/newrelic/newrelic.jar
ADD ./newrelic/newrelic.yml /usr/local/newrelic/newrelic.yml

ENTRYPOINT ["java", "-javaagent:/usr/local/newrelic/newrelic.jar", "-jar", "java-app.jar"]