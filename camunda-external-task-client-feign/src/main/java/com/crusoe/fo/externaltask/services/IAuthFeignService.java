package com.crusoe.fo.externaltask.services;

import com.crusoe.fo.usercenter.dto.RoleDTO;

import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * RoleFeignService
 */
@FeignClient(value = "oauth2-service")
public interface IAuthFeignService {
    @GetMapping(value = "/auth/token")
    // ?username=admin&password=123456&grant_type=password&scope=web&client_id=client_1&client_secret=123456
    public String getToken(@RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("grant_type") String grant_type, @RequestParam("scope") String scope, @RequestParam("client_id") String client_id,
            @RequestParam("client_secret") String client_secret);
}