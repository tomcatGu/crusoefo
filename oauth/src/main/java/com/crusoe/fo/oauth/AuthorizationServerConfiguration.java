package com.crusoe.fo.oauth;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import com.crusoe.fo.oauth.service.UserDetailsServiceImpl;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	private static final String DEMO_RESOURCE_ID = "order";

	@Autowired

	AuthenticationManager authenticationManager;
	@Autowired
	RedisConnectionFactory redisConnectionFactory;
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Primary
	@Bean
	DefaultTokenServices tokenServices() {
		DefaultTokenServices d = new DefaultTokenServices();
		d.setAccessTokenValiditySeconds(600);// 设置token有效期
		d.setRefreshTokenValiditySeconds(1000);
		d.setTokenStore(tokenStore());
		d.setReuseRefreshToken(false);// 是否重复使用token
		d.setSupportRefreshToken(true);// 是否支持refresh token
		return d;
	}

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
		String finalPassword = new BCryptPasswordEncoder().encode("123456");
		clients.inMemory().withClient("client_1").resourceIds(DEMO_RESOURCE_ID)
				.authorizedGrantTypes("client_credentials", "authorization_code", "password", "refresh_token")
				.scopes("web").authorities("res1").secret(finalPassword).and().withClient("webapp")
				.resourceIds(DEMO_RESOURCE_ID).authorizedGrantTypes("authorization_code", "refresh_token")
				.redirectUris("http://localhost:6601/login/oauth2/code/crusoe",
						"http://10.0.0.21:6601/login/oauth2/code/crusoe")
				.scopes("server").authorities("USER").secret(finalPassword);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		// 自定义token生成方式
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));

		endpoints

				// .tokenStore(new RedisTokenStore(redisConnectionFactory))
				// .tokenStore(new InMemoryTokenStore())
				// .tokenServices(tokenServices())
				.tokenStore(tokenStore()).userDetailsService(userDetailsService)
				.authenticationManager(authenticationManager).tokenEnhancer(tokenEnhancerChain)
				// .tokenEnhancer(tokenEnhancerChain)

				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

		// 配置tokenServices参数
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(endpoints.getTokenStore());
		tokenServices.setSupportRefreshToken(false);
		tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
		tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
		tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
		endpoints.tokenServices(tokenServices);

	}

	@Override

	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {

		// 允许表单认证
		oauthServer.allowFormAuthenticationForClients();

		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()");

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

	@Bean
	public TokenEnhancer tokenEnhancer() {
		/*
		 * { "access_token": "f067da15-91f9-4fda-bbe4-6344ae3aefa7", "token_type":
		 * "bearer", "refresh_token": "592dc245-ab20-4433-9060-247ca1f3c6d4",
		 * "expires_in": 43199, "scope": "scope", "username": "guest", "data": { "s1":
		 * "123", "d1": 123.456 } }
		 */
		return (OAuth2AccessToken accessToken, OAuth2Authentication authentication) -> {
			if (accessToken instanceof DefaultOAuth2AccessToken) {
				DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
				Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();
				additionalInformation.put("username", authentication.getPrincipal());
				// additionalInformation.put("data",new Test("123",123.456));
				token.setAdditionalInformation(additionalInformation);

			}

			return accessToken;
		};

	}

}
