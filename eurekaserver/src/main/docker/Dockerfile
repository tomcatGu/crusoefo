FROM java:8
RUN mkdir /app
WORKDIR /app
COPY /eurekaserver-0.0.1.jar /app
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/eurekaserver-0.0.1.jar", "--spring.profiles.active=docker"]
EXPOSE 8761