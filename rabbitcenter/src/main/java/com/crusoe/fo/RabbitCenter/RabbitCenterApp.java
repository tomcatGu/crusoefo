package com.crusoe.fo.RabbitCenter;

import com.crusoe.fo.RabbitCenter.channel.OrderChannel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;


@SpringBootApplication
@EnableBinding(value = {OrderChannel.class})
public class RabbitCenterApp {
	public static void main(String[] args) {
		SpringApplication.run(RabbitCenterApp.class, args);
	}
}
