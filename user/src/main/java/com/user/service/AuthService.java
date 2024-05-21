package com.user.service;

import com.user.dto.RegisterRequest;
import com.user.entity.UserCredential;
import com.user.exception.NotUniqueEmailException;
import com.user.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserCredentialRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String saveUser(RegisterRequest registerRequest) {
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        try {
            repository.save(new UserCredential(registerRequest.getFullName(),registerRequest.getEmail(),registerRequest.getPassword()));
        }catch (DataIntegrityViolationException ex){
            throw new NotUniqueEmailException();
        }
        return "user added to the system";
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
         jwtService.validateToken(token);
    }
}