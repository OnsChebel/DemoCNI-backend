package com.enicar.demo.controller;

import com.enicar.demo.dto.ChangePasswordRequest;
import com.enicar.demo.dto.FormateurDTO;
import com.enicar.demo.mapper.FormateurMapper;
import com.enicar.demo.model.Cycle;
import com.enicar.demo.model.Formateur;
import com.enicar.demo.model.Participant;
import com.enicar.demo.repository.CycleRepository;
import com.enicar.demo.repository.FormateurRepository;
import com.enicar.demo.repository.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/formateurs")
@CrossOrigin(origins = "http://localhost:4200")
public class FormateurController {

    @Autowired
    private FormateurRepository formateurRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public FormateurController(FormateurRepository formateurRepository,
                               CycleRepository cycleRepository,
                               InscriptionRepository inscriptionRepository) {
        this.formateurRepository = formateurRepository;
        this.cycleRepository = cycleRepository;
        this.inscriptionRepository = inscriptionRepository;
    }

    @GetMapping("/cycles/{cycleId}/participants")
    public ResponseEntity<List<Participant>> getParticipantsByCycle(@PathVariable Integer cycleId) {
        List<Participant> participants = inscriptionRepository.findParticipantsByCycleId(cycleId);
        participants.forEach(p -> p.setPassword(null));
        return ResponseEntity.ok(participants);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String login = credentials.get("login");
        String password = credentials.get("password");

        return formateurRepository.findByLogin(login)
                .filter(f -> f.getPassword() != null && passwordEncoder.matches(password, f.getPassword()))
                .map(f -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("id", f.getId());
                    response.put("nom_prenom", f.getNom_prenom() != null ? f.getNom_prenom() : "");
                    response.put("isFirstLogin", f.getIsFirstLogin() != null ? f.getIsFirstLogin() : true);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).body(Map.of("message", "Identifiants incorrects")));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        return formateurRepository.findById(request.getFormateurId()).map(f -> {
            if (!passwordEncoder.matches(request.getAncienPassword(), f.getPassword())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Ancien mot de passe incorrect"));
            }
            f.setPassword(passwordEncoder.encode(request.getNouveauPassword()));
            f.setIsFirstLogin(false);
            formateurRepository.save(f);
            return ResponseEntity.ok(Map.of("message", "Mot de passe mis à jour avec succès"));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/mes-cycles")
    public ResponseEntity<List<Cycle>> getMesCycles(@PathVariable Integer id) {
        return formateurRepository.findById(id).map(formateur -> {
            String nomPrenom = formateur.getNom_prenom();
            List<Cycle> cycles = cycleRepository.findByFormateurName(nomPrenom);
            return ResponseEntity.ok(cycles);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<FormateurDTO> getAllFormateurs() {
        return formateurRepository.findAll()
                .stream()
                .map(FormateurMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormateurDTO> getFormateurById(@PathVariable Integer id) {
        return formateurRepository.findById(id)
                .map(FormateurMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/new-formateur")
    public ResponseEntity<FormateurDTO> createFormateur(@RequestBody FormateurDTO formateurDTO) {
        Formateur formateur = FormateurMapper.toEntity(formateurDTO);
        formateur.setIsFirstLogin(true);

        if (formateur.getPassword() != null && !formateur.getPassword().isBlank()) {
            formateur.setPassword(passwordEncoder.encode(formateur.getPassword()));
        }

        Formateur savedFormateur = formateurRepository.save(formateur);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FormateurMapper.toDTO(savedFormateur));
    }

    @PutMapping("/update-formateur/{id}")
    public ResponseEntity<FormateurDTO> updateFormateur(@PathVariable Integer id, @RequestBody FormateurDTO formateurDetailsDTO) {
        return formateurRepository.findById(id)
                .map(formateur -> {
                    formateur.setNom_prenom(formateurDetailsDTO.getNom_prenom());
                    formateur.setSpecialite(formateurDetailsDTO.getSpecialite());
                    formateur.setDirection(formateurDetailsDTO.getDirection());
                    formateur.setEntreprise(formateurDetailsDTO.getEntreprise());

                    if (formateurDetailsDTO.getLogin() != null) {
                        formateur.setLogin(formateurDetailsDTO.getLogin());
                    }

                    if (Boolean.TRUE.equals(formateur.getIsFirstLogin())
                            && formateurDetailsDTO.getPassword() != null
                            && !formateurDetailsDTO.getPassword().isBlank()) {

                        formateur.setPassword(passwordEncoder.encode(formateurDetailsDTO.getPassword()));
                    }

                    Formateur updatedFormateur = formateurRepository.save(formateur);
                    return ResponseEntity.ok(FormateurMapper.toDTO(updatedFormateur));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete-formateur/{id}")
    public ResponseEntity<Void> deleteFormateur(@PathVariable Integer id) {
        return formateurRepository.findById(id)
                .map(formateur -> {
                    formateurRepository.delete(formateur);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}