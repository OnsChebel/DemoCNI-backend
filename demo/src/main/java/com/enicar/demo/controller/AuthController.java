package com.enicar.demo.controller;

import com.enicar.demo.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
            );

            String token = jwtUtils.generateToken(loginRequest.getLogin());
            return ResponseEntity.ok(Map.of("token", token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Identifiants invalides"));
        }
    }

    public static class LoginRequest {
        private String login;
        private String password;

        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}