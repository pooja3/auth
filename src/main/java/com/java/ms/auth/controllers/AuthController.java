package com.java.ms.auth.controllers;

import com.java.ms.auth.models.entity.User;
import com.java.ms.auth.models.request.LoginCredentials;
import com.java.ms.auth.models.response.LongCredentialResponse;
import com.java.ms.auth.repository.UserRepository;
import com.java.ms.auth.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody User user) {
        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), "user");
        return Collections.singletonMap("jwt-token", token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginHandler(@RequestBody LoginCredentials body) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());

            Authentication authentication = authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(body.getEmail(), body.getUserRole());

            return ResponseEntity.ok().body(
                    LongCredentialResponse.builder()
                            .emailId(body.getEmail())
                            .userRole(authentication.getAuthorities().toString())
                            .jsonWebToken(token)
                            .build());
        } catch (AuthenticationException authExc) {
            throw new RuntimeException("Invalid Login Credentials");
        }
    }

}