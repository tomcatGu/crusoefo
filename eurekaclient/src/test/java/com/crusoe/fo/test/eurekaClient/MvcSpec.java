package com.crusoe.fo.test.eurekaClient;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.crusoe.fo.eurekaClient.controller.HelloController;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MvcSpec.class)
public  class MvcSpec {
	//@Autowired
	//WebApplicationContext context;

	@Before
	public void setup() {
		// RestAssuredMockMvc.webAppContextSetup(this.context);
		RestAssuredMockMvc.standaloneSetup(new HelloController());
	}
}
