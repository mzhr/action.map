FROM java:8-alpine

COPY target/uberjar/event-map.jar /event-map/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/event-map/app.jar"]
