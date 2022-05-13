package com.flightapp.admin.service;

import com.flightapp.admin.entity.Admin;
import com.flightapp.admin.exception.CustomException;
import com.flightapp.admin.repository.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    AdminRepo repo;

    public boolean login(Admin admin) {
        Admin info = fetchAdminDetails(admin.getUsername());
        if(!info.getPassword().equals(admin.getPassword())) {
            throw new CustomException("Invalid Credentials");
        }
        return true;
    }

    public Admin fetchAdminDetails(String username) {
        Optional<Admin> adminInfo = repo.findByUsername(username);
        if(!adminInfo.isPresent()) {
            throw new CustomException("Username not found in Database");
        }
        return adminInfo.get();
    }

    public boolean addAdmin(Admin admin) {
        String encryptPassword = hashPassword(admin.getPassword());
        admin.setPassword(encryptPassword);
        repo.save(admin);
        return true;
    }

    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt(10);
        String hashed_password = BCrypt.hashpw(password, salt);
        return(hashed_password);
    }
}
