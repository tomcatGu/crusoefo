package com.crusoe.fo.gatewayservice.filter;

import java.text.ParseException;

import com.nimbusds.jose.util.JSONObjectUtils;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import io.netty.handler.codec.base64.Base64Decoder;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import reactor.core.publisher.Mono;
@Slf4j
@Component
public class JwtCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			String jwtToken = exchange.getRequest().getHeaders().getFirst("Authorization");
			// 校验jwtToken的合法性;
			if (jwtToken != null) {
				jwtToken = jwtToken.replace("Bearer ", "");
				int firstdot = jwtToken.indexOf(".");
				int lastdot = jwtToken.indexOf(".", firstdot + 1);
				String token = jwtToken.substring(firstdot + 1, lastdot);
				byte[] btoken = Base64Utils.decodeFromString(token);
				try {
					JSONObject jsonObject = (JSONObject) JSONObjectUtils.parse(new String(btoken));
					// System.out.println(jsonObject.get("username"));
					String cacheToken = stringRedisTemplate.opsForValue().get(jsonObject.get("username"));

					if (jwtToken.equals(cacheToken)) {

						return chain.filter(exchange);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			// 不合法(响应未登录的异常)
			ServerHttpResponse response = exchange.getResponse();
			// 设置headers
			HttpHeaders httpHeaders = response.getHeaders();
			httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
			httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
			// 设置body
			String warningStr = "未登录或登录超时";
			DataBuffer bodyDataBuffer = response.bufferFactory().wrap(warningStr.getBytes());

			return response.writeWith(Mono.just(bodyDataBuffer));
		};

	}

}
