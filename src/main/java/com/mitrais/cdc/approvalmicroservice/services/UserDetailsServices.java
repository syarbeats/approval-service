package com.mitrais.cdc.approvalmicroservice.services;


import com.mitrais.cdc.approvalmicroservice.entity.User;
import com.mitrais.cdc.approvalmicroservice.repository.UserRepository;
import com.mitrais.cdc.approvalmicroservice.security.jwt.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class UserDetailsServices implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        return new UserDetails(user);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
