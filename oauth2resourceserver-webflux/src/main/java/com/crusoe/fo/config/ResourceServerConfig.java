package com.crusoe.fo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

//@Configuration
//@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    public static final String RESOURCE_ID = "order";

    //@Override
    public void configure(ResourceServerSecurityConfigurer resources) {

        resources.resourceId(RESOURCE_ID).tokenServices(tokenService()).stateless(false);
    }

    //@Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().requestMatchers("/message").access("#oauth2.hasScope('web')").and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    // @Bean
    public ResourceServerTokenServices tokenService() {
        String finalPassword = "{bcrypt}" + new BCryptPasswordEncoder().encode("123456");
        RemoteTokenServices service = new RemoteTokenServices();
        service.setCheckTokenEndpointUrl("http://localhost:9000/oauth/check_token");
        service.setClientId("client_1");
        service.setClientSecret(finalPassword);

        return service;
    }

}