package com.crusoe.fo.consumer.feign.service.fallback;

import org.springframework.stereotype.Component;

import com.crusoe.fo.consumer.feign.service.ConsumerFeignService;
@Component
public class FeignServiceHystrix implements ConsumerFeignService {

	@Override
	public String hello(String name) {
		// TODO Auto-generated method stub
		return "Sorry,"+name+",service has fail!";
	}

}
