package com.crusoe.fo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.client.ZeebeClient;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {

    private static final String zeebeAPI = "localhost:26500";

    public static void main(String[] args) {
        // System.out.println( "Hello World!" );

        ZeebeClient client = ZeebeClient.newClientBuilder()
                .gatewayAddress(zeebeAPI)
                // .credentialsProvider(credentialsProvider)
                .usePlaintext()
                .build();
        System.out.println(client.getConfiguration().toString());
    }
}
