package com.company.catalog_api.service;

import java.util.ArrayList;
import java.util.List;

import com.company.catalog_api.dao.ContactsDao;
import com.company.catalog_api.dao.UserDao;
import com.company.catalog_api.model.Contacts;
import com.company.catalog_api.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

@Service
public class UserService {

    @Autowired
    UserDao repo;

    @Autowired
    ContactsDao dao;

    public void registerUser(User user) {
        if (user.getCompany_name() == "" || user.getCompany_name() == null) {
            user.setUserType("customer");
        } else {
            user.setUserType("company");
        }

        user.setEnabled(false);

        user.setVerificationCode(RandomString.make(64));
        repo.save(user);

    }

    public User findUser(String email) {
        List<User> user = repo.findByEmail(email);
        if (user.size() > 0) {
            return user.get(0);
        } else {
            return null;
        }
    }

    public User findUserByCompany(String company) {
        List<User> user = repo.findByCompanyName(company);
        if (user.size() > 0) {
            return user.get(0);
        } else {
            return null;
        }
    }

    public List<ObjectNode> getAllCompanyServices() {
        List<User> users = repo.findByUserType("company");
        List<ObjectNode> user_services = new ArrayList<>();
        for (var reg_user : users) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("companyName", reg_user.getCompany_name());
            objectNode.put("serviceName", reg_user.getService_name());
            objectNode.put("serviceDetails", reg_user.getDetails());
            objectNode.put("logo", reg_user.getLogo());
            objectNode.put("email", reg_user.getEmail());
            user_services.add(objectNode);
        }

        return user_services;
    }

    public List<Contacts> getAllCompanyContacts(String companyName) {
        return dao.findByCompany(companyName);
    }

    public String addUserContact(Contacts contact) {
        User user = findUserByCompany(contact.getCompany());
        try {
            if (user != null) {
                dao.save(contact);
                return "added";
            } else {
                return "error";
            }
        } catch (Exception err) {
            return "error";
        }

    }

}
