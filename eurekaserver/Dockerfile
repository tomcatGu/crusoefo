FROM java:8

VOLUME /tmp

ARG JAR_FILE
ADD ./${JAR_FILE} app.jar

WORKDIR /app
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar", "--spring.profiles.active=peer1"]
EXPOSE 8761