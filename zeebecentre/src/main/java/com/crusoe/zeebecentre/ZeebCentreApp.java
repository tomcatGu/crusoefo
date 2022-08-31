package com.crusoe.zeebecentre;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import io.camunda.zeebe.spring.client.EnableZeebeClient;

@SpringBootApplication
@EnableZeebeClient
@EnableScheduling
@EnableAsync
@EnableSpringDataWebSupport
@EnableDiscoveryClient
public class ZeebCentreApp 
{
    public static void main( String[] args )
    {
        //System.out.println( "Hello World!" );
        SpringApplication.run(ZeebCentreApp.class,args);
        //System.out.println( "Hello World!" );
    }

    @Bean
    public ScheduledExecutorService scheduledExecutor() {
      return Executors.newSingleThreadScheduledExecutor();
    }
  
    @Bean
    public Executor asyncExecutor() {
      final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(1);
      executor.setMaxPoolSize(1);
      executor.setQueueCapacity(32);
      executor.initialize();
      return executor;
    }
}
