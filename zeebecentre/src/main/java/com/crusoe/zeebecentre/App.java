package com.crusoe.zeebecentre;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.camunda.zeebe.spring.client.EnableZeebeClient;

@SpringBootApplication
@EnableZeebeClient
@EnableScheduling
@EnableAsync
@EnableSpringDataWebSupport
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
