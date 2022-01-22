package com.crusoe.fo.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class CamundaResourceServerConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().mvcMatcher("/**").authorizeRequests().antMatchers("/rest/**").authenticated()
                //.access("hasAuthority('SCOPE_message.read')")
                .and().oauth2ResourceServer().jwt()
                .decoder(jwtDecoder(jwkSource()));
        ;
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
	 */
	//@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

    /**
     * @Autowired Token tokenStore ;
     * @Autowired JwtAccessTokenConverter jwtAccessTokenConverter;
     * 
     * @Override public void configure(HttpSecurity http) throws Exception {
     *           http.csrf().disable() .authorizeRequests()
     *           .antMatchers("/actuator/**").permitAll()
     *           .antMatchers("/rest/**").authenticated()
     *           .antMatchers("/**").authenticated(); }
     * 
     * @Override public void configure(ResourceServerSecurityConfigurer resources)
     *           throws Exception { resources.tokenStore(tokenStore()); }
     * 
     * @Bean public TokenStore tokenStore(){
     * 
     *       return new JwtTokenStore(jwtTokenEnhancer()); }
     * 
     * @Bean public JwtAccessTokenConverter jwtTokenEnhancer(){
     *       JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
     *       Resource resource = new ClassPathResource("crusoe.cert");
     * 
     *       String publicKey; try { publicKey = new
     *       String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
     *       }catch (IOException e) { throw new RuntimeException(e); }
     * 
     *       converter.setVerifierKey(publicKey); return converter; }
     
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
    */

}