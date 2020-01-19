package com.crusoe.fo.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	// @Bean
	// public UserDetailsService userDetailsService() {
	// return new UserService();
	// }

	// password 方案三：支持多种编码，通过密码的前缀区分编码方式,推荐

	@Bean
	PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}

	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http
		// .formLogin().loginPage("/login").permitAll()
		// .and()
		// .requestMatchers()
		// .antMatchers("/", "/login", "/oauth/authorize",
		// "/oauth/confirm_access")
		// .and()
		// .authorizeRequests()
		// .anyRequest().authenticated();

		// http.requestMatchers()
		// .antMatchers("/login", "/oauth/authorize")
		// .and()
		// .authorizeRequests()
		// .anyRequest().authenticated()
		// .and()
		// .formLogin().permitAll();

		// http.csrf().disable();
		// 不拦截 oauth 开放的资源
		// http.requestMatchers().anyRequest().and().authorizeRequests().antMatchers("/oauth/**").permitAll();
		http.formLogin().and().requestMatchers().anyRequest().and().authorizeRequests().antMatchers("/oauth/**").permitAll();
		http.csrf().disable();

	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
