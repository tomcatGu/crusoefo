package com.crusoe.fo.externaltask.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import feign.RequestInterceptor;

@Configuration
@EnableAsync
public class BeanConfig {
    @Value("${security.oauth2.client.accessTokenUri}")
    private String tokenUrl;

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    @Value("${security.oauth2.client.grant-type}")
    private String grantType;

    @Value("${security.oauth2.client.scope}")
    private List<String> scopes;

    @Value("${security.oauth2.client.username}")

    private String username;

    @Value("${security.oauth2.client.password}")
    private String password;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(5);
        // 设置最大线程数
        executor.setMaxPoolSize(10);
        // 设置队列容量
        executor.setQueueCapacity(20);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("hello-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    // 下面3个Bean 配合 application.yml 对应的EndPoint 需要加上 #oauth2.hasScope('server')
    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    protected OAuth2ProtectedResourceDetails resource() {
        ResourceOwnerPasswordResourceDetails resource;
        resource = new ResourceOwnerPasswordResourceDetails();


        resource.setAccessTokenUri(tokenUrl);
        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        resource.setGrantType(grantType);
        resource.setScope(scopes);
        resource.setUsername(username);
        resource.setPassword(password);
        return resource;
    }

    @Bean
    public OAuth2RestTemplate clientCredentialsRestTemplate() {

        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        return new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(atr));

    }
}