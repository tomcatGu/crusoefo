package com.crusoe.fo.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages="com.crusoe.fo")
@EnableDiscoveryClient
public class Oauth2ServerApp {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApp.class, args);
    }
}

	// @Bean
	// public ConsumerTokenServices consumerTokenServices() {
	// 	return new DefaultTokenServices();

	// }