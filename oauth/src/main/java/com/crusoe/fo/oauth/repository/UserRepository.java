package com.crusoe.fo.oauth.repository;

import com.crusoe.fo.oauth.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);
}
