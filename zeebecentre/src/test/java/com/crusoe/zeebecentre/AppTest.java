package com.crusoe.zeebecentre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crusoe.zeebecentre.rest.ProcessResource;

/**
 * Unit test for simple App.
 */
@ExtendWith (SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        Assertions.assertTrue( true );
    }
    @Autowired
    private ProcessResource processResourceController;
    @Autowired
	private MockMvc mockMvc;

}
