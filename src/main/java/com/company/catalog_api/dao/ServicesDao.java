package com.company.catalog_api.dao;

import com.company.catalog_api.model.Services;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicesDao extends JpaRepository<Services,String> {

    
    
}
