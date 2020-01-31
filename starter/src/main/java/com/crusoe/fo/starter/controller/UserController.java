package com.crusoe.fo.starter.controller;

import com.crusoe.fo.starter.domain.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController(value = "/user")
public class UserController {

    @GetMapping(value = "/")
    public Flux<User> getAll() {

        return null;
    }

}