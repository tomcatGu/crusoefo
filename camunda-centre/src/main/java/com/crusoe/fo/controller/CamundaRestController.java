package com.crusoe.fo.controller;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
public class CamundaRestController {

    @Autowired
    ProcessEngine processEngine;
    

    @GetMapping(value="repository")
    @ResponseBody
    public List<String> getRepository() {
//processEngine.getRepositoryService()

        return new ArrayList<String>();
    }
    
}
