package com.example.spring_jwt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring_jwt.dto.UserRegisterDTO;
import com.example.spring_jwt.model.UserAuthority;
import com.example.spring_jwt.model.UserEntity;
import com.example.spring_jwt.repository.UserEntityRepository;

@Service
public class UserEntityService {

    private final UserEntityRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserEntityService(UserEntityRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserEntity> findByUsername(String username) {
        return this.repository.findByUsername(username);
    }

    public UserEntity save(UserRegisterDTO userDTO){
        UserEntity user= new UserEntity(
            null,
            userDTO.username(),
            this.passwordEncoder.encode(userDTO.password()),
            userDTO.email(),
            List.of(UserAuthority.READ)
        );
        return this.repository.save(user);
    }
}
