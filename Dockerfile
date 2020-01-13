FROM gcr.io/distroless/java:8
WORKDIR /app
VOLUME /tmp
COPY target/*.jar app.jar
CMD ["app.jar"]
