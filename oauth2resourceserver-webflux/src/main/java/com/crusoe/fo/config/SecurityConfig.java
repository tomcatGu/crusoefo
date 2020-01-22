package com.crusoe.fo.config;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.web.reactive.config.EnableWebFlux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@Slf4j
@EnableReactiveMethodSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
		http.authorizeExchange()

				// .pathMatchers("/order/message").hasAuthority("res1")
				.pathMatchers("/**").permitAll().anyExchange().authenticated().and().oauth2ResourceServer().jwt()
				.publicKey(getRSAPublicKey("crusoe.cer")).jwtAuthenticationConverter(grantedAuthoritiesExtractor());;
		//
		return http.build();
	}

	Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
		JwtAuthenticationConverter jwtAuthenticationConverter =
				new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter
				(new GrantedAuthoritiesExtractor());
		return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
	}

	static class GrantedAuthoritiesExtractor
        implements Converter<Jwt, Collection<GrantedAuthority>> {

    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<String> authorities = (Collection<String>)
                jwt.getClaims().get("authorities");

        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}

	// 将byte数组变成RSAPublicKey
	public RSAPublicKey getRSAPublicKey(String keyFile) {

		Resource resource = new ClassPathResource(keyFile);
		X509Certificate x509Certificate = null;
		CertificateFactory certificateFactory = null;
		try {
			certificateFactory = CertificateFactory.getInstance("X.509");
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			x509Certificate = (X509Certificate) certificateFactory.generateCertificate(resource.getInputStream());
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("x509Certificate_getIssuerDN_发布方标识名___:" + x509Certificate.getIssuerDN());
		System.out.println("x509Certificate_getPublicKey_公钥___:" + x509Certificate.getPublicKey());
		return (RSAPublicKey) x509Certificate.getPublicKey();

	}
}