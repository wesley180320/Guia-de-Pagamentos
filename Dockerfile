FROM openjdk:8
WORKDIR /app
COPY ./target/Gerador-de-Pagamento-0.0.1-SNAPSHOT.jar ./guiaPagamentos.jar
EXPOSE 8081

ENTRYPOINT java -jar guiaPagamentos.jar
