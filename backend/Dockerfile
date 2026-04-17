FROM eclipse-temurin:25-jre

WORKDIR /app

COPY target/app.jar app.jar
COPY docker-entrypoint.sh docker-entrypoint.sh

RUN chmod +x docker-entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["./docker-entrypoint.sh"]