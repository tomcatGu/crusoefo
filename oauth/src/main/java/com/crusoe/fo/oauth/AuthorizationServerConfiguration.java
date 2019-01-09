package com.crusoe.fo.oauth;

import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

@Configuration
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	private static final String DEMO_RESOURCE_ID = "order";

	@Configuration

	@EnableResourceServer

	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		@Override

		public void configure(ResourceServerSecurityConfigurer resources) {

			resources.resourceId(DEMO_RESOURCE_ID).stateless(true);

		}

		@Override

		public void configure(HttpSecurity http) throws Exception {

			http

					.authorizeRequests()

					.antMatchers("/order/**").authenticated();// 配置order访问控制，必须认证过后才可以访问

		}

	}

	@Autowired

	AuthenticationManager authenticationManager;
	@Autowired
	RedisConnectionFactory redisConnectionFactory;

	/*
	 * @Autowired private RedisConnectionFactory connectionFactory;
	 * 
	 * @Bean public RedisTokenStore tokenStore() { return new
	 * RedisTokenStore(connectionFactory); }
	 */

	// @Autowired
	// @Qualifier("dataSource")
	// private DataSource dataSource;

	// @Bean(name = "dataSource")
	// @ConfigurationProperties(prefix = "spring.datasource")
	// public DataSource dataSource() {
	// return DataSourceBuilder.create().build();
	// }

	// @Bean("jdbcTokenStore")
	// public JdbcTokenStore getJdbcTokenStore() {
	// return new JdbcTokenStore(dataSource);
	// }

	// @Bean
	// public UserDetailsService userDetailsService(){
	// return new UserService();
	// }

	/*
	 * 配置客户端详情信息(内存或JDBC来实现)
	 *
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// 配置两个客户端,一个用于password认证一个用于client认证
		String finalPassword = "{bcrypt}" + new BCryptPasswordEncoder().encode("123456");
		clients.inMemory()
				.withClient("client_1").resourceIds(DEMO_RESOURCE_ID)
				.authorizedGrantTypes("client_credentials", "refresh_token").scopes("select").authorities("oauth2")
				.secret(finalPassword)
				.and()
				.withClient("webapp").resourceIds(DEMO_RESOURCE_ID)
				.authorizedGrantTypes("implicit", "refresh_token").scopes("server").authorities("oauth2")
				.secret(finalPassword);
		// 初始化 Client 数据到 DB
		// clients.jdbc(dataSource)
		/*
		 * clients.inMemory().withClient("client").authorizedGrantTypes(
		 * "client_credentials") .scopes("all", "read",
		 * "write").authorities("client_credentials").accessTokenValiditySeconds (7200)
		 * .secret(passwordEncoder.encode("123456"))
		 * 
		 * .and().withClient("client_2").authorizedGrantTypes("password",
		 * "refresh_token") .scopes("all", "read",
		 * "write").accessTokenValiditySeconds(7200).refreshTokenValiditySeconds (10000)
		 * .authorities("password").secret(passwordEncoder.encode("123456"))
		 * 
		 * .and().withClient("client_3").authorities("authorization_code",
		 * "refresh_token")
		 * .secret(passwordEncoder.encode("123456")).authorizedGrantTypes(
		 * "authorization_code") .scopes("all", "read",
		 * "write").accessTokenValiditySeconds(7200).refreshTokenValiditySeconds (10000)
		 * .redirectUris("http://localhost:8080/callback",
		 * "http://localhost:8080/signin")
		 * 
		 * .and().withClient("client_test").secret(passwordEncoder.encode( "123456"))
		 * .authorizedGrantTypes("all flow") .authorizedGrantTypes("authorization_code",
		 * "client_credentials", "refresh_token", "password", "implicit")
		 * .redirectUris("http://localhost:8080/callback",
		 * "http://localhost:8080/signin") .scopes("all", "read",
		 * "write").accessTokenValiditySeconds(7200).refreshTokenValiditySeconds
		 * (10000);
		 */
		// https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql
		// clients.withClientDetails(new JdbcClientDetailsService(dataSource));
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		endpoints

				// .tokenStore(new RedisTokenStore(redisConnectionFactory))
				.tokenStore(new InMemoryTokenStore())

				.authenticationManager(authenticationManager).accessTokenConverter(jwtAccessTokenConverter())

				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
		/*
		 * JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		 * converter.setSigningKey("123");
		 * 
		 * endpoints.authenticationManager(authenticationManager).
		 * accessTokenConverter(converter) // 配置 JwtAccessToken 转换器 //
		 * .accessTokenConverter(jwtAccessTokenConverter()) // refresh_token 需要
		 * UserDetailsService is required // .userDetailsService(userDetailsService)
		 * .allowedTokenEndpointRequestMethods(HttpMethod.GET,
		 * HttpMethod.POST).tokenStore(tokenStore());
		 */
	}

	@Override

	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {

		// 允许表单认证

		oauthServer.tokenKeyAccess("permitAll()").allowFormAuthenticationForClients();

	}

	/**
	 * 使用非对称加密算法来对Token进行签名
	 * 
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("crusoe.jks"), "tomtom1982".toCharArray())
				.getKeyPair("crusoe");
		converter.setKeyPair(keyPair);
		return converter;
	}

}
