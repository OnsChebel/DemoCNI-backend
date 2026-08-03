package com.enicar.demo.security;

import com.enicar.demo.model.Administrateur;
import com.enicar.demo.model.Participant;
import com.enicar.demo.repository.AdministrateurRepository;
import com.enicar.demo.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Administrateur> adminOpt = administrateurRepository.findByLogin(username);
        if (adminOpt.isPresent()) {
            Administrateur admin = adminOpt.get();
            return new User(
                    admin.getLogin(),
                    admin.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        Optional<Participant> participantOpt = participantRepository.findByMail(username);
        if (participantOpt.isPresent()) {
            Participant participant = participantOpt.get();
            return new User(
                    participant.getMail(),
                    participant.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))
            );
        }

        throw new UsernameNotFoundException("Utilisateur non trouvé avec l'identifiant : " + username);
    }
}