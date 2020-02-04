package com.crusoe.fo.starter.controller;

import com.crusoe.fo.starter.domain.User;
import com.crusoe.fo.starter.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/")
    public Mono<String> getAll() {
        return userRepository.testFindId();

    }

}