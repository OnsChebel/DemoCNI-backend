package com.enicar.demo.controller;

import com.enicar.demo.dto.LoginRequestDTO;
import com.enicar.demo.dto.AuthResponseDTO;
import com.enicar.demo.dto.ParticipantRegisterDTO;
import com.enicar.demo.model.Formateur;
import com.enicar.demo.model.Participant;
import com.enicar.demo.repository.AdministrateurRepository;
import com.enicar.demo.repository.FormateurRepository;
import com.enicar.demo.repository.ParticipantRepository;
import com.enicar.demo.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private FormateurRepository formateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
            );

            String token = jwtUtils.generateToken(loginRequest.getLogin());

            String role = "ADMIN";
            Object userObj = null;

            Optional<Participant> participant = participantRepository.findByMail(loginRequest.getLogin());
            Optional<Formateur> formateur = formateurRepository.findByLogin(loginRequest.getLogin());

            if (participant.isPresent()) {
                role = "PARTICIPANT";
                Participant p = participant.get();
                p.setPassword(null);
                userObj = p;
            } else if (formateur.isPresent()) {
                role = "FORMATEUR";
                Formateur f = formateur.get();
                f.setPassword(null);
                userObj = f;
            } else {
                userObj = administrateurRepository.findByLogin(loginRequest.getLogin()).orElse(null);
            }

            AuthResponseDTO response = AuthResponseDTO.builder()
                    .token(token)
                    .role(role)
                    .user(userObj)
                    .build();

            return ResponseEntity.ok(response);
        }
        catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Identifiants invalides"));
        }
    }

    @PostMapping("/register-participant")
    public ResponseEntity<?> registerParticipant(@RequestBody ParticipantRegisterDTO registerDTO) {
        if (participantRepository.existsByMail(registerDTO.getMail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Cet e-mail est déjà utilisé !"));
        }

        Participant participant = Participant.builder()
                .nom_prenom(registerDTO.getNom_prenom())
                .cin(registerDTO.getCin())
                .mail(registerDTO.getMail())
                .tel_port(registerDTO.getTel_port())
                .entreprise(registerDTO.getEntreprise())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .build();

        participantRepository.save(participant);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Compte créé avec succès !"));
    }
}