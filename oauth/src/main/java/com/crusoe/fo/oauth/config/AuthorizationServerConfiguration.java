package com.crusoe.fo.oauth.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.crusoe.fo.oauth.service.UserDetailsServiceImpl;
import com.crusoe.fo.usercenter.entity.User;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
//import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
//import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
//import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
//import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
//import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
//import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
//import org.springframework.security.oauth2.server.authorization.config.ProviderSettings.Builder;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

//@Configuration(proxyBeanMethods = false)
@Configuration
// @EnableAuthorizationServer
//@Import(OAuth2AuthorizationServerConfiguration.class)
@EnableWebSecurity
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	// @Autowired
	// AuthenticationManager authenticationManager;
	@Autowired
	RedisConnectionFactory redisConnectionFactory;
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// @Bean
	// public ConsumerTokenServices consumerTokenServices() {
	// return new DefaultTokenServices();

	// }

		/**
	 * 个性化 JWT token
	 */
	class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

		@Override
		public void customize(JwtEncodingContext context) {
			// 添加一个自定义头
			UsernamePasswordAuthenticationToken oAuth2Authentication = context.getPrincipal();
			UserDetails user=(UserDetails)oAuth2Authentication.getPrincipal();

			context.getClaims().claim("username",user.getUsername() );
			
			Collection<GrantedAuthority> authorities = oAuth2Authentication.getAuthorities();
			List<String> authorityList = new ArrayList<String>();
			for (GrantedAuthority grantedAuthority : authorities) {
				authorityList.add(grantedAuthority.getAuthority());
			}

			context.getClaims().claim("authorities", authorityList);
			//context.getClaims().claim("username", user.getUsername());

			context.getClaims().claim("department", ((User) user).getDepartment().getName());

			context.getClaims().claim("code", 20000); //

			context.getClaims().claim("clientid", context.getRegisteredClient().getClientId());
		}
	}


	/**
	 * 对jwt token 进行增强，如果有需要的话
	 *
	 * @return oauth 2 token customizer
	 */
	// @Bean
	OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
		return jwtEncodingContext -> {
			JwtClaimsSet.Builder claims = jwtEncodingContext.getClaims();
			claims.claim("xxxx", "xxxxx");
			JwtEncodingContext.with(jwtEncodingContext.getJwsHeader(), claims);
		};
	}
	/***
	 * @Override public void configure( ClientDetailsServiceConfigurer clients )
	 *           throws Exception { clients.inMemory() .withClient("client")
	 *           .authorizedGrantTypes("password") .secret("{noop}secret")
	 *           .scopes("all"); }
	 */
	/**
	 * Authorization server 集成
	 *
	 * @param http the http
	 * @return the security filter chain
	 * @throws Exception the exception
	 */
	// @Bean
	// @Order(Ordered.HIGHEST_PRECEDENCE)
	// public SecurityFilterChain
	// authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
	// // Authorization Server 默认配置
	// this.defaultOAuth2AuthorizationServerConfigurer(http);
	// return http.formLogin(Customizer.withDefaults()).build();
	// }
	/**
	 * void defaultOAuth2AuthorizationServerConfigurer(HttpSecurity http) throws
	 * Exception { OAuth2AuthorizationServerConfigurer<HttpSecurity>
	 * authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();
	 * // TODO 你可以根据需求对authorizationServerConfigurer进行一些个性化配置 RequestMatcher
	 * authorizationServerEndpointsMatcher =
	 * authorizationServerConfigurer.getEndpointsMatcher();
	 * 
	 * // 拦截 授权服务器相关的请求端点 http.requestMatcher(authorizationServerEndpointsMatcher)
	 * .authorizeRequests().anyRequest() .permitAll() //.authenticated() .and() //
	 * 忽略掉相关端点的csrf .csrf(csrf ->
	 * csrf.ignoringRequestMatchers(authorizationServerEndpointsMatcher)) //
	 * 开启form登录 //.formLogin().and() // 应用 授权服务器的配置
	 * .apply(authorizationServerConfigurer); }
	 */
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

	/**
	 * 注册一个客户端应用
	 *
	 * @param jdbcTemplate the jdbc template
	 * @return the registered client repository
	 */
	/**
	 * //@SneakyThrows //@Bean public RegisteredClientRepository
	 * registeredClientRepository() { // TODO 生产上 注册客户端需要使用接口 不应该采用下面的方式
	 * RegisteredClient registeredClient =
	 * RegisteredClient.withId(UUID.randomUUID().toString()) // 客户端ID和密码
	 * .clientId("felord-client") .clientSecret("secret") // 名称 可不定义
	 * .clientName("felord") // 授权方法
	 * .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
	 * // 授权类型 .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
	 * .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
	 * .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) //
	 * 回调地址名单，不在此列将被拒绝 而且只能使用IP或者域名 不能使用 localhost
	 * .redirectUri("http://127.0.0.1:8080/login/oauth2/code/felord-client-oidc")
	 * .redirectUri("http://127.0.0.1:8080/authorized")
	 * .redirectUri("http://127.0.0.1:8080/foo/bar")
	 * .redirectUri("https://baidu.com") // OIDC支持 .scope(OidcScopes.OPENID) //
	 * 其它Scope .scope("message.read") .scope("message.write") // JWT的配置项 包括TTL
	 * 是否复用refreshToken等等 .tokenSettings(TokenSettings.builder().build()) //
	 * 配置客户端相关的配置项，包括验证密钥或者 是否需要授权页面
	 * .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
	 * .build();
	 * 
	 * // 每次都会初始化 生产的话 只初始化JdbcRegisteredClientRepository RegisteredClientRepository
	 * registeredClientRepository = registeredClientRepository();
	 * registeredClientRepository.save(registeredClient); return
	 * registeredClientRepository; }
	 */
/**
     * 定义 Spring Security 的拦截器链
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // 设置jwt token个性化
        http.setSharedObject(OAuth2TokenCustomizer.class, new CustomOAuth2TokenCustomizer());
        // 授权服务器配置
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        return http
		        .securityMatcher(endpointsMatcher)
                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
				.cors().and()
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .apply(authorizationServerConfigurer)
                .and()
                .formLogin()
                .and()
                .build();
    }


	// 创立默认的bean 登录客户端,基于 受权码、 刷新令牌的能力
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient client = RegisteredClient.withId("pig").clientId("pig")
				.clientSecret(passwordEncoder.encode("pig"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantTypes(authorizationGrantTypes -> {
					authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
					authorizationGrantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
					authorizationGrantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
				}).tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(1))
						.refreshTokenTimeToLive(Duration.ofDays(3)).reuseRefreshTokens(true).build()

				).redirectUri("http://192.168.1.104:9526").build();
		return new InMemoryRegisteredClientRepository(client);
	}

	/***
	 * @Override
	 * 
	 *           public void configure(AuthorizationServerSecurityConfigurer
	 *           oauthServer) {
	 * 
	 *           // 允许表单认证 oauthServer.allowFormAuthenticationForClients();
	 * 
	 *           oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()");
	 * 
	 *           }
	 */

	/**
	 * 对JWT进行签名的 加解密密钥
	 */
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
	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	/**
	 * 配置一些断点的路径，比如：获取token、授权端点 等
	 */
	/**
	 * 配置一些断点的路径，比如：获取token、授权端点 等
	 */
	@Bean
	public AuthorizationServerSettings  providerSettings() {
		return AuthorizationServerSettings .builder()
				// 配置获取token的端点路径
				.tokenEndpoint("/oauth2/token").build();
				// 发布者的url地址,一般是本系统访问的根路径
				// 此处的 qq.com 需要修改我们系统的 host 文件
				//.issuer("http://qq.com:8080");
	}

	/**
	 * @Bean public TokenEnhancer tokenEnhancer() {
	 * 
	 *       return (OAuth2AccessToken accessToken, OAuth2Authentication
	 *       authentication) -> { if (accessToken instanceof
	 *       DefaultOAuth2AccessToken) { DefaultOAuth2AccessToken token =
	 *       (DefaultOAuth2AccessToken) accessToken; Map<String, Object>
	 *       additionalInformation = new LinkedHashMap<String, Object>();
	 *       //additionalInformation.put("username", authentication.getDetails());
	 *       User user=(User)authentication.getUserAuthentication().getPrincipal();
	 *       List<GrantedAuthority> authorities = (List<GrantedAuthority>)
	 *       authentication.getAuthorities(); List<String> authorityList = new
	 *       ArrayList<String>(); for (GrantedAuthority grantedAuthority :
	 *       authorities) { authorityList.add(grantedAuthority.getAuthority()); }
	 * 
	 *       additionalInformation.put("authorities", authorityList);
	 *       additionalInformation.put("username",user.getUsername());
	 * 
	 *       additionalInformation.put("department",user.getDepartment().getName());
	 * 
	 *       additionalInformation.put("code", 20000); //
	 *       additionalInformation.put("data",new Test("123",123.456));
	 *       token.setAdditionalInformation(additionalInformation);
	 * 
	 *       }
	 * 
	 *       return accessToken; };
	 * 
	 *       }
	 */

}
