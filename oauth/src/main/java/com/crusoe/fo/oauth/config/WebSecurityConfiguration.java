package com.crusoe.fo.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


	@Bean
	PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}

	

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

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}



}
