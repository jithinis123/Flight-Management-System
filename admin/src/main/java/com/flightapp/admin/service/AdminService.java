package com.flightapp.admin.service;

import com.flightapp.admin.entity.Admin;
import com.flightapp.admin.exception.AdminException;
import com.flightapp.admin.repository.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    AdminRepo repo;

    public boolean login(Admin admin) {
        Admin info = fetchAdminDetails(admin.getUsername());
        if(!info.getPassword().equals(admin.getPassword())) {
            throw new AdminException("Invalid Credentials");
        }
        return true;
    }

    public Admin fetchAdminDetails(String username) {
        Optional<Admin> adminInfo = repo.findByUsername(username);
        if(!adminInfo.isPresent()) {
            throw new AdminException("Username not found in Database");
        }
        return adminInfo.get();
    }

    public boolean addAdmin(Admin admin) {
        repo.save(admin);
        return true;
    }
}
