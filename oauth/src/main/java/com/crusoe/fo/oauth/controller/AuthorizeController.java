package com.crusoe.fo.oauth.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizeController {
    @Autowired
    @Qualifier("consumerTokenServices")
	public ConsumerTokenServices consumerTokenServices() {
		return new DefaultTokenServices();

	}

    @Autowired
    private UserDetailsService userDetailsService;

    //@Autowired
    //TokenEndpoint tokenEndpoint;

    @GetMapping("/user/logout")
    public String logout(@RequestParam String token) {
        //if (consumerTokenServices.revokeToken(token)) {
            return "注销成功";
        
    }
    @GetMapping("/uri")
    public String getURI(){

        return "OK";
    }

    /**
     * 获取用户凭证（供客户端使用）
     *
     * @param principal
     * @return
     */
    @GetMapping("/user")
    public Map<String,String> user(Principal principal) {
        //System.out.println(principal);
        if (principal != null) {
            UsernamePasswordAuthenticationToken oAuth2Authentication = (UsernamePasswordAuthenticationToken) principal;
            
            
            UserDetails user=(UserDetails)oAuth2Authentication.getPrincipal();
            //logger.info("details = " + details);  // id, email, name, link etc.
            Map<String, String> map = new LinkedHashMap<>();
            map.put("user_name", user.getUsername());
            return map;
        }
        return null;
    }
}
