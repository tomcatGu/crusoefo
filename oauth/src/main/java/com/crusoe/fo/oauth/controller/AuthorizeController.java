package com.crusoe.fo.oauth.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizeController {
    @Autowired
    @Qualifier("consumerTokenServices")
    ConsumerTokenServices consumerTokenServices;

    @Autowired
    TokenEndpoint tokenEndpoint;

    @GetMapping("/user/logout")
    public String logout(@RequestParam String token) {
        if (consumerTokenServices.revokeToken(token)) {
            return "注销成功";
        } else {
            return "注销失败";
        }
    }

    /**
     * 获取用户凭证（供客户端使用）
     *
     * @param principal
     * @return
     */
    @GetMapping("/user")
    public Map<String,String> user(Principal principal) {
        if (principal != null) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            Map<String, String> details = new LinkedHashMap<>();
            details = (Map<String, String>) authentication.getDetails();
            //logger.info("details = " + details);  // id, email, name, link etc.
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", details.get("username"));
            return map;
        }
        Map<String, String> a = new LinkedHashMap<>();
        a.put("user_name", "crusoe");
        return a;
    }
}
