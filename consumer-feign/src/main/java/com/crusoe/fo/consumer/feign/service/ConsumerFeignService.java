package com.crusoe.fo.consumer.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.crusoe.fo.consumer.feign.service.fallback.FeignServiceHystrix;

@FeignClient(value = "eureka-producer",fallback=FeignServiceHystrix.class)
public interface ConsumerFeignService {

	@GetMapping("hello")
	public String hello(@RequestParam(value = "name") String name);
}
