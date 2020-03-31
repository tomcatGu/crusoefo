package com.crusoe.fo.externaltask.services;

import java.util.Collections;
import java.util.LinkedHashMap;

import javax.annotation.Resource;

import com.crusoe.fo.externaltask.config.FeignTokenInterceptor;
import com.crusoe.fo.usercenter.dto.RoleDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.interceptor.ClientRequestContext;
import org.camunda.bpm.client.interceptor.ClientRequestInterceptor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RoleFeignService {
    // @Resource
    IRoleFeignService roleService;
    @Resource
    IAuthFeignService authService;

    public RoleFeignService(IRoleFeignService service) {
        this.roleService = service;
    }

    @Async
    public void startCreateRoleTask() {
        ClientRequestInterceptor interceptor = new ClientRequestInterceptor() {

            @Override
            public void intercept(ClientRequestContext requestContext) {
                // TODO Auto-generated method stub
                // String token = authService.getToken("admin", "123456", "password", "web",
                // "client_1", "123456");

                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                map.add("username", "admin");
                map.add("password", "123456");
                map.add("grant_type", "password");
                map.add("scop", "web");
                map.add("client_id", "client_1");
                map.add("client_secret", "123456");
                LinkedHashMap<String, String> token = (LinkedHashMap<String, String>) authService.generateToken(map);

                log.info(token.get("access_token"));
                // ObjectMapper mapper = new ObjectMapper();
                // JsonNode node;

                // node = mapper.readTree(token);
                requestContext.addHeader("Authorization", "Bearer " + token.get("access_token"));

            }
        };
        ExternalTaskClient client = ExternalTaskClient.create().baseUrl("http://localhost:8787/rest")
                .addInterceptor(interceptor).build();
        log.info("External Task Client start...");

        // subscribe to the topic
        client.subscribe("creditScoreChecker").lockDuration(1000).handler((externalTask, externalTaskService) -> {

            // retrieve a variable from the Workflow Engine
            String rolename = externalTask.getVariable("rolename");
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setRolename(rolename);
            roleDTO = roleService.createRole(roleDTO);

            // List<Integer> creditScores = new ArrayList<>(Arrays.asList(rolename, 9, 1, 4,
            // 10));

            // create an object typed variable
            // ObjectValue creditScoresObject =
            // Variables.objectValue(creditScores).create();

            // complete the external task
            externalTaskService.complete(externalTask, Collections.singletonMap("role", roleDTO));

            System.out.println("The External Task " + externalTask.getId() + " has been completed!");

        }).open();
    }

}