package com.company.catalog_api.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
public class Product {

    @Id
    private int code;

    private String name;

    private String image;

    private String brand;

    private float price;

    private String description;

    @ManyToMany
    private Set<Services> servicability=new HashSet<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonManagedReference
    public Set<Services> getServicability() {
        return servicability;
    }

    public void setServicability(Set<Services> servicability) {
        this.servicability = servicability;
    }

    @Override
    public String toString() {
        return "Product [brand=" + brand + ", code=" + code + ", description=" + description + ", image=" + image
                + ", name=" + name + ", price=" + price + ", servicability=" + servicability + "]";
    }

    
    

    

    
}
