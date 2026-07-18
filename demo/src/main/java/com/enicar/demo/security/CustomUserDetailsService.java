package com.enicar.demo.security;

import com.enicar.demo.model.Administrateur;
import com.enicar.demo.repository.AdministrateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdministrateurRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Administrateur admin = adminRepository.findById(username)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("Administrateur introuvable");
                });


        return new User(
                admin.getLogin(),
                admin.getPassword(),
                Collections.emptyList()
        );
    }
}