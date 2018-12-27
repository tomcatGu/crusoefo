package com.crusoe.fo.eurekaClient.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@RequestMapping("/hello")
	public String hello(@RequestParam String name) {
		
		return "hello,"+name;
	}
	
	@RequestMapping("/hello2")
	public Map hello2(@RequestParam String name) {
		HashMap hm=new HashMap();
		hm.put("code", "000000");
		hm.put("mesg", "abc");
		return hm;
	}

}
