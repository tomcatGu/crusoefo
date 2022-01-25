package com.crusoe.fo.oauth.config;

import java.util.ArrayList;
import java.util.List;

import com.crusoe.fo.usercenter.entity.User;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration{
	/**
	 * 个性化 JWT token
	 */
	class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

		@Override
		public void customize(JwtEncodingContext context) {
			// 添加一个自定义头
			User user = (User) context.getPrincipal();
			context.getHeaders().header("username", user.getUsername());
			List<GrantedAuthority> authorities = (List<GrantedAuthority>) ((UserDetails) context.getAuthorization())
					.getAuthorities();
			List<String> authorityList = new ArrayList<String>();
			for (GrantedAuthority grantedAuthority : authorities) {
				authorityList.add(grantedAuthority.getAuthority());
			}

			context.getClaims().claim("authorities", authorityList);
			context.getClaims().claim("username", user.getUsername());

			context.getClaims().claim("department", user.getDepartment().getName());

			context.getClaims().claim("code", 20000); //

			context.getHeaders().header("client-id", context.getRegisteredClient().getClientId());
		}
	}


	@Bean
	PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.setSharedObject(OAuth2TokenCustomizer.class, new CustomOAuth2TokenCustomizer());
        http.authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .formLogin().and().cors().and().csrf().disable();
        return http.build();
    }
	
/**
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.formLogin().and()
		.cors()
		.and()
		.authorizeRequests()
		.requestMatchers(CorsUtils::isPreFlightRequest)
		.permitAll()
		.and()
		.requestMatchers().anyRequest()
		.and().authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll().antMatchers("/oauth/**").permitAll();
		//.antMatchers(HttpMethod.OPTIONS, "/oauth/**").permitAll()
		//.and().cors().and().csrf().disable();
		

	}
	
	//@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
*/



}
