FROM eclipse-temurin:8-jdk

WORKDIR /app

COPY ./target/Gerador-de-Pagamento-0.0.1-SNAPSHOT.jar guiaPagamentos.jar

EXPOSE 8081

ENTRYPOINT ["java", "-Dspring.profiles.active=homol", "-jar", "guiaPagamentos.jar"]
