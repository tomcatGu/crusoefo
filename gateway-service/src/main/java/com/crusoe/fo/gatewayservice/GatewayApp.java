package com.crusoe.fo.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


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
