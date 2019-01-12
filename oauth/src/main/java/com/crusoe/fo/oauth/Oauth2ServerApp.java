package com.crusoe.fo.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
public class Oauth2ServerApp {
	public static void main(String[] args) {
		SpringApplication.run(Oauth2ServerApp.class, args);
	}
}
