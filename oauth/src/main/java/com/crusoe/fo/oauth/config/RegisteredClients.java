package com.crusoe.fo.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@Component
//@ConfigurationProperties(prefix="registeredClients")
public class RegisteredClients{

    public static class Client {
        public String clientId;
        public String clientSecret;
        public String scope;
        public String authorizedGrantTypes;
        public String authorities;
        public String redirectUris;
        //public String accessTokenValiditySeconds;
        //public String refreshTokenValiditySeconds;


    }

    
}