FROM java:8
RUN mkdir /app
WORKDIR /app
ARG JAR_FILE
COPY ${JAR_FILE} /app
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/eurekaserver-0.0.1.jar", "--spring.profiles.active=peer1"]
EXPOSE 8761