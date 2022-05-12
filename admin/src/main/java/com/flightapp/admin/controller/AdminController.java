package com.flightapp.admin.controller;

import com.flightapp.admin.entity.Admin;
import com.flightapp.admin.model.JwtRequest;
import com.flightapp.admin.model.JwtResponse;
import com.flightapp.admin.service.AdminService;
import com.flightapp.admin.service.JwtUserDetailsService;
import com.flightapp.admin.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Admin adminDetails) throws Exception {

        authenticate(adminDetails.getUsername(), adminDetails.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(adminDetails.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus(){
        return new ResponseEntity<>("Admin application is running", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Admin admin) {
        service.login(admin);
        return new ResponseEntity<>("Login Successful", HttpStatus.OK);
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<String> addAdmin(@RequestBody Admin admin) {
        service.addAdmin(admin);
        return new ResponseEntity<>("Admin added successfully", HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @ExceptionHandler
    public ResponseEntity<String> handleControllerException(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }
}
