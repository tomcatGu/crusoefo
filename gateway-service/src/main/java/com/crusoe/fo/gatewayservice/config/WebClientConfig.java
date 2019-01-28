package com.crusoe.fo.gatewayservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration

public class WebClientConfig {

    @Bean
    WebClient webClient(LoadBalancerExchangeFilterFunction eff,ReactiveClientRegistrationRepository clientRegistrationRepository,
                        ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                clientRegistrationRepository, authorizedClientRepository);
        oauth.setDefaultOAuth2AuthorizedClient(true);
        return WebClient.builder().filter(eff).filter(oauth).build();
    }

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryReactiveClientRegistrationRepository(this.crusoeClientRegistration());
    }

    private ClientRegistration crusoeClientRegistration() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("OAUTH2-SERVICE");
        System.out.println(serviceInstance.getHost());
        System.out.println(serviceInstance.getPort());
        String host=serviceInstance.getHost();
        int port=serviceInstance.getPort();
        String URI="http://OAUTH2-SERVICE";
        //String URI="http://"+host+":"+String.valueOf(port);
        return ClientRegistration.withRegistrationId("crusoe")
                .clientId("webapp")
                .clientSecret("123456")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("server")
                .authorizationUri(URI+"/oauth/authorize")
                .tokenUri(URI+"/oauth/token")
                .userInfoUri(URI+"/user")
                .userNameAttributeName("user_name")
                .jwkSetUri(URI+"/oauth/token_key")
                .clientName("crusoe")
                .build();
    }


}
