package com.crusoe.fo.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApp {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(GatewayApp.class, args);
	}

	@Bean
	SecurityWebFilterChain configure(ServerHttpSecurity http) throws Exception {
		http.authorizeExchange().pathMatchers("/", "/public/**").permitAll().anyExchange().authenticated().and()
				.oauth2Login().and().oauth2Client();
		return http.build();
	}

}
