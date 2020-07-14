package com.crusoe.fo.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

public class GatewayApp {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(GatewayApp.class, args);
	}


}
