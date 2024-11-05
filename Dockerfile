FROM eclipse-temurin:21-jre
COPY build/libs/SnippetSearcher-0.0.1-SNAPSHOT.jar java-app.jar
ENTRYPOINT ["java", "-jar", "java-app.jar"]
