package com.crusoe.fo.eurekaServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import com.netflix.eureka.EurekaBootStrap;

/**
 * Hello world!
 *
 */

@EnableEurekaServer
@SpringBootApplication 
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(EurekaServerApplication.class, args);
    }
}
