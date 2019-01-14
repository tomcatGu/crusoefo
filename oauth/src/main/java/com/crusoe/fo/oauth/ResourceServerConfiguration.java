package com.crusoe.fo.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration

@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String DEMO_RESOURCE_ID = "order";
    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;


    @Override

    public void configure(HttpSecurity http) throws Exception {

        http.formLogin().and()
                // .successHandler(appLoginInSuccessHandler))//如果有必要，在这里可以自定义成功处理器
                // .failureHandler(appLoginFailureHandler)//如果有必要，在这里可以自定义错误处理器
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and().authorizeRequests()
                .antMatchers("/open/**", "/login**").permitAll()// 开放的资源不用授权
                .anyRequest().authenticated();// 其他任何请求都需要授权

    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {

        resources.resourceId(DEMO_RESOURCE_ID).stateless(true).tokenStore(new JwtTokenStore(jwtAccessTokenConverter));
    }

}
