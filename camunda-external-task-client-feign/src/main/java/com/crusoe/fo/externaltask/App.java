package com.crusoe.fo.externaltask;

import com.crusoe.fo.externaltask.services.RoleFeignService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableAsync // 开启多任务
public class App {

    public static void main(String[] args) {

        ConfigurableApplicationContext context=SpringApplication.run(App.class, args);
        RoleFeignService startTask=context.getBean(RoleFeignService.class);
        startTask.startCreateRoleTask();
        
    }
    
}