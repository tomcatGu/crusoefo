package com.crusoe.fo.starter.repositories;

import java.sql.Connection;
import java.sql.DriverManager;

import org.davidmoten.rx.jdbc.ConnectionProvider;
import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.pool.NonBlockingConnectionPool;
import org.davidmoten.rx.jdbc.pool.Pools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UserRepository {

    private Database db = null;

    public UserRepository(@Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password) throws Exception {
        // Connection connection = DriverManager.getConnection(url, username, password);
        NonBlockingConnectionPool pool = Pools.nonBlocking().maxPoolSize(Runtime.getRuntime().availableProcessors() * 5)
                .connectionProvider(ConnectionProvider.from(url, username, password)).build();

        this.db = Database.from(pool);
    }

    public Flux<String> testFindId(Pageable page) {
        Flowable<String> logs = db.select("select author from act_co_databasechangelog").getAs(String.class).take(page.getPageSize());
        return Flux.from(logs);
    }


}