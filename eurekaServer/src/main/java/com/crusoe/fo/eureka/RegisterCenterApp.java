package com.crusoe.fo.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import com.netflix.eureka.EurekaBootStrap;


@EnableEurekaServer
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RegisterCenterApp 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(RegisterCenterApp.class, args);
    }
}
