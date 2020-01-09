package com.crusoe.fo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2ResourceServerController {
	@GetMapping("/")
	public String index(@AuthenticationPrincipal Jwt jwt) {
		//return String.format("Hello, %s!", jwt.getExpiresAt());
		return "root";
	}

	@GetMapping("/message")
	@PreAuthorize("hasAnyAuthority('res1')")
	public String message() {
		return "secret message";
	}
}
