package com.crusoe.zeebecentre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.alibaba.fastjson2.JSON;
import com.crusoe.zeebecentre.rest.ProcessResource;
import com.crusoe.zeebecentre.rest.dto.DeploymentDto;
import com.crusoe.zeebecentre.rest.dto.FileDto;







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
     * @throws Exception
     */
    @Test
    public void shouldAnswerWithTrue() throws Exception
    {
        //Assertions.assertTrue( true );

        String bpmn =
        Files.readString(
            Paths.get(this.getClass().getClassLoader().getResource("payment.bpmn").getPath().substring(1)));

        DeploymentDto deploymentDto=new DeploymentDto();
        FileDto fileDto=new FileDto();
        fileDto.setFilename("test.bpmn");
        fileDto.setMimeType("bpmn");
        fileDto.setContent(bpmn.getBytes());
        List<FileDto> files=new ArrayList<FileDto>();
        files.add(fileDto);
        deploymentDto.setFiles(files);
        String requestJson = JSON.toJSONString(deploymentDto);
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8089/api/processes/").content(requestJson).accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }
    @Autowired
    private ProcessResource processResourceController;
    @Autowired
	private MockMvc mockMvc;

}
