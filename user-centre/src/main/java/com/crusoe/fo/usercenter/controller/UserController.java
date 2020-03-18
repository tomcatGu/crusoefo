package com.crusoe.fo.usercenter.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
public class UserController {
   

    @GetMapping(value="repository")
    @ResponseBody
    public List<String> getRepository() {
//processEngine.getRepositoryService()

        return new ArrayList<String>();
    }
    
}
