package com.example.spring_jwt.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.spring_jwt.service.UserEntityService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    Logger log= LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserEntityService userService;

    public UserDetailsServiceImpl(UserEntityService userService) {
        this.userService = userService;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("loadUserByUsername: {}", username);
        return this.userService.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException(username + " no encontrado")
        );
    }

}
