package com.company.catalog_api.dao;

import java.util.List;

import com.company.catalog_api.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, String> {

    List<User> findByEmail(String email);

    List<User> findByUserType(String type);

    @Query("FROM User u WHERE u.company_name=?1")
    List<User> findByCompanyName(String company);

}
