package com.crusoe.fo.test.eurekaClient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)

@SpringBootTest(classes=ProducerAppTest.class)
public class ProducerAppTest

{

	@Test
	public void testApp() {
		// assertTrue( true );
	}
}
