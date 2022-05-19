package com.company.catalog_api.dao;

import java.util.List;

import com.company.catalog_api.model.Contacts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactsDao extends JpaRepository<Contacts, String> {
    List<Contacts> findByCompany(String companyName);

}
