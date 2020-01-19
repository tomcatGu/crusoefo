package com.crusoe.fo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value="/order")
public class OAuth2ResourceServerController {
	@GetMapping("/")
	public Mono<String> index(@AuthenticationPrincipal Jwt jwt) {
		//return String.format("Hello, %s!", jwt.getExpiresAt());
		return Mono.just("hello world");
	}

	@GetMapping("/message")
	@PreAuthorize("hasAnyAuthority('res123')")
	public String message() {
		return "secret message";
	}
}
