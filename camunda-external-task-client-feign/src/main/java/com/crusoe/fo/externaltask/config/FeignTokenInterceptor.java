package com.crusoe.fo.externaltask.config;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignTokenInterceptor implements RequestInterceptor {
    @Resource
    OAuth2RestTemplate authRestemplate;

    @Override
    public void apply(RequestTemplate template) {

        // tring token = authService.getToken("admin", "123456", "password", "web",
        // "client_1", "123456");
        // ObjectMapper mapper = new ObjectMapper();
        // JsonNode node;
        // try {
        // node = mapper.readTree(token);
        template.header("Authorization", "Bearer " + authRestemplate.getAccessToken().getValue());
        // } catch (JsonMappingException e) {
        // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (JsonProcessingException e) {
        // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

    }

}