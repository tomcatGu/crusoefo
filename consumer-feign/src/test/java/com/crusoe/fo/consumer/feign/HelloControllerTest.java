package com.crusoe.fo.consumer.feign;


import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;



@RunWith(SpringRunner.class)
//springboot的测试启动类，需要依赖spring-boot-test库
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//初使化测试测试配置，测试controller需要
@AutoConfigureMockMvc
//启动契约服务，模拟produer提供服务
@AutoConfigureStubRunner(ids = {"com.crusoe.fo:eurekaclient:+:stubs:9000"},stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@DirtiesContext
public class HelloControllerTest {
	 @Autowired
	    private MockMvc mvc;

	    @Test
	    public void testMethod() throws Exception {
	        mvc.perform(MockMvcRequestBuilders.get("/hellohello/zhangsan"))
	                .andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.jsonPath("code", Is.is("000000")))
	        .andExpect(MockMvcResultMatchers.jsonPath("mesg", Is.is("处理成功")));
	    }
}
