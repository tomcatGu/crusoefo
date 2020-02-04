package com.crusoe.fo.starter.repositories;

import java.sql.Connection;
import java.sql.DriverManager;

import org.davidmoten.rx.jdbc.ConnectionProvider;
import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.pool.NonBlockingConnectionPool;
import org.davidmoten.rx.jdbc.pool.Pools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class UserRepository {

    private Database db = null;

    public UserRepository(@Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password) throws Exception {
        // Connection connection = DriverManager.getConnection(url, username, password);
        NonBlockingConnectionPool pool = Pools.nonBlocking().maxPoolSize(Runtime.getRuntime().availableProcessors() * 5)
                .connectionProvider(ConnectionProvider.from(url,username,password)).build();

        this.db = Database.from(pool);
    }

    public Mono<String> testFindId() {

        db.select("select author from act_co_databasechangelog")
        .getAs(String.class)
        .blockingForEach(System.out::println);
        return Mono.just("data");
    }

}