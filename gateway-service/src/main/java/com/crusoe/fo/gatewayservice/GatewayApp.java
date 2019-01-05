package com.crusoe.fo.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.factory.StripPrefixGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@SpringBootApplication
@EnableDiscoveryClient
//@EnableWebFluxSecurity
public class GatewayApp {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(GatewayApp.class, args);
	}


}
