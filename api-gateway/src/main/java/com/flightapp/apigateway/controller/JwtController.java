package com.flightapp.apigateway.controller;

import com.flightapp.apigateway.model.JwtResponse;
import com.flightapp.apigateway.model.UserInfo;
import com.flightapp.apigateway.service.JwtUserDetailsService;
import com.flightapp.apigateway.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

public class JwtController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @GetMapping("/api/status")
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("Api gateway is running", HttpStatus.OK);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserInfo info) throws Exception {

        authenticate(info.getUsername(), info.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(info.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
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
