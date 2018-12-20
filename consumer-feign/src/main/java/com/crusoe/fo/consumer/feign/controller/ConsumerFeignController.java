package com.crusoe.fo.consumer.feign.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.crusoe.fo.consumer.feign.service.ConsumerFeignService;

@RestController
public class ConsumerFeignController {
	@Resource
	private ConsumerFeignService service;

	@GetMapping("/hello/{name}")
	public String index(@PathVariable("name") String name) {
		return service.hello(name);
	}
}