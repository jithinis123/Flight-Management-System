package com.flightapp.apigateway.service;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

//    @Autowired
//    AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Admin admin = adminService.fetchAdminDetails(username);
        return new User(username, "$2a$10$E/n0qAi.bBA1pzAuEVkYpejDQbihfrlsGkf7czQ.WI/JCi6zBBHmK", new ArrayList<>());
    }
}