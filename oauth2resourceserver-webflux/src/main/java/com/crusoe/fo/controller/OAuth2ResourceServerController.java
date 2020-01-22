package com.crusoe.fo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/order")
public class OAuth2ResourceServerController {
	@GetMapping("/")
	public Mono<String> index(@AuthenticationPrincipal Jwt jwt) {
		// return String.format("Hello, %s!", jwt.getExpiresAt());
		return Mono.just("hello world");
	}

	@GetMapping("/message")
	//@PreAuthorize("hasAnyAuthority('res12','res2','res3')")
	public Mono<Map<String, Object>> message() {
		Map<String, Object> map = new HashMap<>(8); 
		map.put("code", 20000);
		map.put("message","secret message");
		return Mono.just(map);
	}
}
