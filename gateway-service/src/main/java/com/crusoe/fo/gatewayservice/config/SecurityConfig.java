package com.crusoe.fo.gatewayservice.config;

import com.crusoe.fo.gatewayservice.filter.JwtCheckGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig{
	@Bean
	public JwtCheckGatewayFilterFactory jwtCheckGatewayFilterFactory() {
		return new JwtCheckGatewayFilterFactory();
	}

//	 @Bean
//	public MapReactiveUserDetailsService userDetailsService() {
//		UserDetails user = User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build();
//		return new MapReactiveUserDetailsService(user);
//	}

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
		http.authorizeExchange().pathMatchers("/oauth/**").permitAll().anyExchange()
				.authenticated()
				//.and().oauth2Login()
				//.and().oauth2Client()
				.and().oauth2ResourceServer()
				.jwt().publicKey(getRSAPublicKey("crusoe.cer"));
		return http.build();
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
