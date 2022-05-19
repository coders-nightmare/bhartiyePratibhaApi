package com.company.catalog_api.dao;

import java.util.Set;

import com.company.catalog_api.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product,Integer>{

    Set<Product> findByName(String name);
    Set<Product> findByBrand(String brand);

    
}
