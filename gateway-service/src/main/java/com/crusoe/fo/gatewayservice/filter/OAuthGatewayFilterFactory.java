package com.crusoe.fo.gatewayservice.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class OAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object>
{
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    OAuthGatewayFilter oAuthGatewayFilter;
    @Override
    public GatewayFilter apply(Object config)
    {
        return oAuthGatewayFilter;
    }
}