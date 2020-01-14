package com.crusoe.fo.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
//@EnableDiscoveryClient
@EnableEurekaClient
public class GatewayApp {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(GatewayApp.class, args);
	}


}
