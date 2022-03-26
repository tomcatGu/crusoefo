package com.crusoe.fo.gatewayservice.filter;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.util.JSONObjectUtils;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class OAuthGatewayFilter implements GatewayFilter, Ordered {

    private final StringRedisTemplate stringRedisTemplate;

    public OAuthGatewayFilter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                //AtomicReference<String> bodyRef = new AtomicReference<>();
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer join = dataBufferFactory.join(dataBuffers);

                        byte[] content = new byte[join.readableByteCount()];

                        join.read(content);
                        // 释放掉内存
                        DataBufferUtils.release(join);
                        String str = new String(content, Charset.forName("UTF-8"));

                        originalResponse.getHeaders().setContentLength(str.getBytes().length);
                        // log.error("gateway catch service exception error:"+ str);
                        // System.out.println(str);

                        try {
                            JSONObject jwt = (JSONObject) JSONObjectUtils.parse(str);
                            String access_token=jwt.getAsString("access_token");
                            int firstdot=access_token.indexOf('.');
                            int seconddot=access_token.indexOf(".",firstdot+1);
                            String payload=access_token.substring(firstdot+1,seconddot);
                            
                            byte[] btoken = Base64Utils.decodeFromString(payload);
                            JSONObject jsonObject = (JSONObject) JSONObjectUtils.parse(new String(btoken));
                            //System.out.println(stringRedisTemplate.opsForValue().get("admin"));

                            
                            stringRedisTemplate.opsForValue().set(jsonObject.get("username").toString(),
                            access_token);
                            //System.out.println(jsonObject.get("access_token"));
                            //System.out.println(jsonObject.get("username"));
                        } catch (java.text.ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        // JsonResult result = new JsonResult();
                        // result.setCode(ErrorCode.SYS_EXCEPTION.getCode());
                        // result.setMessage(ErrorCode.SYS_EXCEPTION.getMsg());

                        return bufferFactory.wrap(str.getBytes());
                    }));

                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
        // replace response with decorator
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return -2;
    }

}