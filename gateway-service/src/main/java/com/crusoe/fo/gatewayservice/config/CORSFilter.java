package com.crusoe.fo.gatewayservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange swe, WebFilterChain wfc) {
        ServerHttpRequest request = swe.getRequest();
        if (CorsUtils.isCorsRequest(request)) {
            ServerHttpResponse response = swe.getResponse();
            HttpHeaders headers = response.getHeaders();
            //headers.setAccessControlAllowOrigin("*");
            
            headers.add("Access-Control-Allow-Origin", "http://192.168.1.104:9526");
            //headers.setAccessControlAllowCredentials(true);
            headers.add("Access-Control-Allow-Methods", "*");
            //headers.add("Access-Control-Allow-Methods", "POST");
            headers.add("Access-Control-Allow-Header","*");
            headers.add("Access-Control-Max-Age", "3600");
            headers.add("Access-Control-Allow-Headers", "authorization");
            headers.add("Access-Control-Allow-Headers", "Content-Type");
            if (request.getMethod() == HttpMethod.OPTIONS) {
               response.setStatusCode(HttpStatus.OK);
               return Mono.empty();
            }
        }
        return wfc.filter(swe);
    }
}
