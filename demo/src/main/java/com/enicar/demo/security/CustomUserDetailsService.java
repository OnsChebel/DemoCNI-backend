package com.enicar.demo.security;

import com.enicar.demo.model.Administrateur;
import com.enicar.demo.model.Formateur;
import com.enicar.demo.model.Participant;
import com.enicar.demo.repository.AdministrateurRepository;
import com.enicar.demo.repository.FormateurRepository;
import com.enicar.demo.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private FormateurRepository formateurRepository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Administrateur> adminOpt = administrateurRepository.findByLogin(username);
        if (adminOpt.isPresent()) {
            Administrateur admin = adminOpt.get();
            return new User(
                    admin.getLogin(),
                    formatPassword(admin.getPassword()),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        Optional<Participant> participantOpt = participantRepository.findByMail(username);
        if (participantOpt.isPresent()) {
            Participant participant = participantOpt.get();
            return new User(
                    participant.getMail(),
                    formatPassword(participant.getPassword()),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))
            );
        }

        Optional<Formateur> formateurOpt = formateurRepository.findByLogin(username);
        if (formateurOpt.isPresent()) {
            Formateur formateur = formateurOpt.get();
            return new User(
                    formateur.getLogin(),
                    formatPassword(formateur.getPassword()),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_FORMATEUR"))
            );
        }

        throw new UsernameNotFoundException("Utilisateur non trouvé avec l'identifiant : " + username);
    }

    private String formatPassword(String password) {
        if (password == null) return "";
        if (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$")) {
            return password;
        }
        return encoder.encode(password);
    }
}