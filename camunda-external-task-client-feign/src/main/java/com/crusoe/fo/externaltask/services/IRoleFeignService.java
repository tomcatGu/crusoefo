package com.crusoe.fo.externaltask.services;

import com.crusoe.fo.externaltask.config.FeignTokenInterceptor;
import com.crusoe.fo.usercenter.dto.RoleDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * RoleFeignService
 */
@FeignClient(value="user-service",configuration=FeignTokenInterceptor.class)
public interface IRoleFeignService {

    @GetMapping(value="/role/create")
    public RoleDTO createRole(@RequestBody RoleDTO role);
}