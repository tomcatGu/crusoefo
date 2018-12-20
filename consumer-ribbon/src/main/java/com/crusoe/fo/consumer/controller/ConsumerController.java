package com.crusoe.fo.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crusoe.fo.consumer.service.RibbonServiceImpl;

@RestController
public class ConsumerController {
	@Autowired
	RibbonServiceImpl service;
	
	@GetMapping("/say/{name}")
	public String index(@PathVariable String name) {
		//System.out.println("say...");

		return service.sayHello(name);
	}

}
