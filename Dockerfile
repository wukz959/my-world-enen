FROM openjdk:17-alpine
#WORKDIR /
COPY ./target/my-world-enen-0.0.1-SNAPSHOT.jar /app.jar
ENV TZ Asia/Shanghai
EXPOSE 7777
ENTRYPOINT ["java", "-jar", "/app.jar"]