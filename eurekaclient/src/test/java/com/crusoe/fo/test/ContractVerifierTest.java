package com.crusoe.fo.test;


import com.crusoe.fo.eurekaClient.controller.HelloController;
import com.crusoe.fo.test.eurekaClient.ProducerAppTest;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.ResponseOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;
@RunWith(SpringRunner.class)
@SpringBootTest(classes=ContractVerifierTest.class)
public class ContractVerifierTest  {
	@Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(new HelloController());
    }
	@Test
	public void validate_helloController() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();

		// when:
			ResponseOptions response = given().spec(request)
					.queryParam("name","zhangsan")
					.get("/hello2");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).isEqualTo("application/json;charset=UTF-8");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['code']").isEqualTo("000000");
			assertThatJson(parsedJson).field("['mesg']").isEqualTo("abc");
	}

}
