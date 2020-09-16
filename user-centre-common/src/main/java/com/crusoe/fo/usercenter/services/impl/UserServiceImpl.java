package com.crusoe.fo.usercenter.services.impl;

import com.crusoe.fo.usercenter.entity.User;
import com.crusoe.fo.usercenter.repository.UserRepository;
import com.crusoe.fo.usercenter.services.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("UserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User create(User user) {
        // TODO Auto-generated method stub
        return userRepository.save(user);
    }
    
}
