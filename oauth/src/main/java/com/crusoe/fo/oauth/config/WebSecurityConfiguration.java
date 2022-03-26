package com.crusoe.fo.oauth.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.crusoe.fo.usercenter.entity.User;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration{


	@Bean
	PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                        authorizeRequests.antMatchers("/login/**").permitAll().antMatchers("/oauth2/**").permitAll().antMatchers("/js/**", "/css/**", "/images/**").permitAll().anyRequest().authenticated()
                )
                .formLogin().permitAll().and().cors(Customizer.withDefaults()).csrf().disable();
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
