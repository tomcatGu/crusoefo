package com.crusoe.fo.usercenter.controller;

import java.util.ArrayList;
import java.util.List;

import com.crusoe.fo.usercenter.dto.UserDTO;
import com.crusoe.fo.usercenter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
@RequestMapping(value = "/user")
public class UserController {
   @Autowired
   UserRepository userRepository;

    @PostMapping(value="create")
    @ResponseBody
    public List<String> create(@RequestBody UserDTO userDto) {
        
        return new ArrayList<String>();
    }
    
}
