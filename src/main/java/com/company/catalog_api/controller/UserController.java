package com.company.catalog_api.controller;

import java.util.HashMap;
import java.util.List;

import com.company.catalog_api.model.Contacts;
import com.company.catalog_api.model.User;
import com.company.catalog_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserService service;

    @PostMapping(path = "/user/register")
    public ResponseEntity<HashMap<String, String>> createUser(@RequestBody User user) {
        try {
            service.registerUser(user);
            HashMap<String, String> userData = new HashMap<>();
            userData.put("token", user.getVerificationCode());
            return new ResponseEntity<>(userData, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PostMapping(path = "/user/login")
    public ResponseEntity<ObjectNode> signInUser(@RequestBody User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        User reg_user = service.findUser(user.getEmail());
        if (reg_user == null) {
            objectNode.put("message", "user not found");
            return new ResponseEntity<>(objectNode, HttpStatus.UNAUTHORIZED);
        }
        // System.out.println(reg_user.getPassword()+" "+user.getPassword());
        if (reg_user.getPassword().equals(user.getPassword())) {
            reg_user.setEnabled(true);
            objectNode.put("name", reg_user.getName());
            objectNode.put("email", reg_user.getEmail());
            objectNode.put("token", reg_user.getVerificationCode());
            objectNode.put("userType", reg_user.getUserType());
            if (reg_user.getUserType().equals("company")) {
                objectNode.put("companyName", reg_user.getCompany_name());

                objectNode.put("serviceName", reg_user.getService_name());
                objectNode.put("serviceDetails", reg_user.getDetails());
                objectNode.put("logo", reg_user.getLogo());
                objectNode.put("aadharNo", reg_user.getAadharNo());
                objectNode.put("address", reg_user.getAddress());
            }

            return new ResponseEntity<>(objectNode, HttpStatus.OK);
        } else {
            objectNode.put("message", "Invalid Password");
            return new ResponseEntity<>(objectNode, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/user/company/services")
    public ResponseEntity<List<ObjectNode>> getCompanyServices() {
        try {

            return new ResponseEntity<List<ObjectNode>>(service.getAllCompanyServices(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping(path = "/user/company/contacts")
    public ResponseEntity<List<Contacts>> getCompanyContacts(@RequestBody ObjectNode data) {
        try {
            System.out.println(data);
            String companyName = data.get("companyName").textValue();
            return new ResponseEntity<List<Contacts>>(service.getAllCompanyContacts(companyName), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping(path = "/user/contact")
    public ResponseEntity<HashMap<String, String>> addContact(@RequestBody Contacts contact) {
        try {
            String contactRes = service.addUserContact(contact);
            HashMap<String, String> map = new HashMap<>();
            if (!contactRes.equals("error")) {
                map.put("message", "added");
                return new ResponseEntity<>(map, HttpStatus.CREATED);

            } else {
                map.put("message", "company not found");
                return new ResponseEntity<HashMap<String, String>>(map, HttpStatus.CONFLICT);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

}
