package com.company.catalog_api.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
public class Services {

    @Id
    private int pinCode;

    private String deliveryTime;


    @ManyToMany(mappedBy="servicability")
    private Set<Product> product=new HashSet<>();

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @JsonBackReference
    public Set<Product> getProduct() {
        return product;
    }

    public void setProduct(Set<Product> product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Services [deliveryTime=" + deliveryTime + ", pinCode=" + pinCode  + "]";
    }

    
    
}
