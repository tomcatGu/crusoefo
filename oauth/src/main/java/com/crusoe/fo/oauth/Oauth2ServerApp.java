package com.crusoe.fo.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.crusoe.fo")

public class Oauth2ServerApp {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApp.class, args);
    }
}
