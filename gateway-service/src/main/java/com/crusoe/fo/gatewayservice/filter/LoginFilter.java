package com.crusoe.fo.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
//@Component
public class LoginFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub


        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // TODO Auto-generated method stub


        String token=exchange.getRequest().getQueryParams().getFirst("access-token");
        if(token==null){
            //认证失败
            System.out.println("认证失败");
            exchange.getResponse().setStatusCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED) ;
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    
}