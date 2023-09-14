package com.rest.cycles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var myUserEntityOpt = userService.getByName(username);
        if (myUserEntityOpt.isEmpty()) {
            throw new UsernameNotFoundException("No user found with username " + username);
        }
        var myUserEntity = myUserEntityOpt.get();
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        var springSecurityUserEntity = User.withUsername(myUserEntity.getName())
                                            .password(
                                            myUserEntity.getPassword())
                                            .roles(myUserEntity.getRole())
                                            .build();
        return springSecurityUserEntity;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
         PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
         return encoder;
    }
    
}