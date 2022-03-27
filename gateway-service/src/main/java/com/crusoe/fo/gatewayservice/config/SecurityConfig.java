package com.crusoe.fo.gatewayservice.config;

import com.crusoe.fo.gatewayservice.filter.JwtCheckGatewayFilterFactory;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig{

//	 @Bean
//	public MapReactiveUserDetailsService userDetailsService() {
//		UserDetails user = User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build();
//		return new MapReactiveUserDetailsService(user);
//	}

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
		http.authorizeExchange().pathMatchers("/oauth2/**").permitAll().pathMatchers("/auth/**").permitAll().pathMatchers("/actuator/**").permitAll().anyExchange()
				.permitAll()
				//.authenticated()
				.and().cors().and().csrf().disable().authorizeExchange()
				//.and().oauth2Login()
				//.and().oauth2Client()
				//.and().csrf().disable()
				.and().oauth2ResourceServer()
				.jwt().jwtDecoder(jwtDecoder(jwkSource()));
				
				//.publicKey(getRSAPublicKey("crusoe.cer"));
		return http.build();
	}


	@Bean
	public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		// KeyPair keyPair = keyPairGenerator.generateKeyPair();
		KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("crusoe.jks"), "tomtom1982".toCharArray())
				.getKeyPair("crusoe");
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString())
				.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	/**
	 * jwt 解码
	 * @throws NoSuchAlgorithmException
	 */
	//@Bean
	public ReactiveJwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) throws NoSuchAlgorithmException {
		//return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		// KeyPair keyPair = keyPairGenerator.generateKeyPair();
		KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("crusoe.jks"), "tomtom1982".toCharArray())
				.getKeyPair("crusoe");
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString())
				.build();
		return NimbusReactiveJwtDecoder.withPublicKey(publicKey)
                .signatureAlgorithm(SignatureAlgorithm.RS256)
                .build();
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
