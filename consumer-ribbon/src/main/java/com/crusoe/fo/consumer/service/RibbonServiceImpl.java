package com.crusoe.fo.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RibbonServiceImpl {
	@Autowired
	RestTemplate restTemplate;
	
	public String sayHello(String name){
		return restTemplate.getForObject("http://eurekaclient/hello?name="+name,String.class);

		
	}

}
