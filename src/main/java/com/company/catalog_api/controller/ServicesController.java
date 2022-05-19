package com.company.catalog_api.controller;

import java.util.List;

import com.company.catalog_api.dao.ServicesDao;
import com.company.catalog_api.model.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicesController {

    @Autowired
    ServicesDao repo;

    @GetMapping("/services")
    public List<Services> getServices(){
        return repo.findAll();
    }
    
}
