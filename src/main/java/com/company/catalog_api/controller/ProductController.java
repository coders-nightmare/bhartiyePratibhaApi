package com.company.catalog_api.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.company.catalog_api.dao.ProductDao;
import com.company.catalog_api.model.Product;
import com.company.catalog_api.model.Services;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
public class ProductController {
    @Autowired
    ProductDao repo;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){
        try{
            return new ResponseEntity<List<Product>>(repo.findAll(), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(null,HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id,@RequestHeader  HashMap<String,String> headers){
        
        try{
        String authorizationHeader=headers.get("authorizaion");
            if(authorizationHeader.length()==0){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            var token=authorizationHeader.split(" ")[1];
            if(token==null){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            if(token.length()!=64){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }


            int int_id=Integer.parseInt(id);
            Optional<Product> p=repo.findById(int_id);
            if(p.isPresent()){
                // System.out.println(p.get());
                return new ResponseEntity<>(p.get(),HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    public Product getProductById(String id){
        try{
            int int_id=Integer.parseInt(id);
            Optional<Product> p=repo.findById(int_id);
            if(p.isPresent()){
                System.out.println(p.get());
                return p.get();
            }
            else{
                return null;
            }
        }
        catch(Exception e){
            return null;
        }
    }

    @GetMapping("/product/{id}/{pincode}")
    public ResponseEntity<ObjectNode> servicible(@PathVariable("id") String id,@PathVariable("pincode") String pincode){
        ObjectMapper mapper=new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        try{
            int pinCode=Integer.parseInt(pincode);
            var product=getProductById(id);
        if(product!=null ){
            List<Services> filterService=product.getServicability().stream().filter(obj->obj.getPinCode()==pinCode).collect(Collectors.toList());
            System.out.println(filterService);
            if(filterService.size()>0){
                objectNode.put("servicability",true);
                objectNode.put("deliveryTime",filterService.get(0).getDeliveryTime());
                return new ResponseEntity<>(objectNode,HttpStatus.OK);
            }
            else{
                
                objectNode.put("servicabiltity",false);
                return new ResponseEntity<>(objectNode,HttpStatus.NO_CONTENT);
            }
        }
        else{
            return new ResponseEntity<>(objectNode,HttpStatus.NO_CONTENT);
        }
        }
        catch(Exception e){
            return new ResponseEntity<>(objectNode,HttpStatus.NOT_FOUND);
        }
        
    }

    @GetMapping("/product/{id}/price")
    public ResponseEntity<ObjectNode> getProductPrice(@PathVariable("id") String id){
        ObjectMapper mapper=new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        try{
            var product=getProductById(id);
        if(product!=null ){
        
            objectNode.put("price",product.getPrice());
            return new ResponseEntity<>(objectNode,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(objectNode,HttpStatus.NO_CONTENT);
        }
    }
    catch(Exception e){
        return new ResponseEntity<>(objectNode,HttpStatus.NOT_FOUND);
    }

    }

    @PostMapping(path="/products/search")
    public ResponseEntity<Set<Product>> getProductByText(@RequestBody Map<String,Object> data){

        Set<Product> products=new HashSet<>();
        try{
            int code=(Integer) data.get("code");
            String name=(String) data.get("name");
            String brand=(String) data.get("brand");
            var product=getProductById(Integer.toString(code));
            if(product!=null){
                products.add(product);
            }
            if(brand!=null)
            {
                Set<Product> new_products=repo.findByBrand(brand.toLowerCase());
                if(new_products.size()>0)
                products.addAll(new_products);
            }
            if(name!=null){
                Set<Product> new_products=repo.findByName(name.toLowerCase());
                if(new_products.size()>0)
                products.addAll(new_products);

            }
            return new ResponseEntity<>(products,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(products,HttpStatus.NOT_FOUND);
        }
    }  
    
}
