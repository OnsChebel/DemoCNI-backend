package com.enicar.demo.controller;

import com.enicar.demo.dto.InscriptionInfoDTO;
import com.enicar.demo.dto.ParticipantDTO;
import com.enicar.demo.mapper.ParticipantMapper;
import com.enicar.demo.model.Participant;
import com.enicar.demo.repository.InscriptionRepository;
import com.enicar.demo.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/participants")
@CrossOrigin(origins = "http://localhost:4200")
public class ParticipantController {
    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @GetMapping
    public List<ParticipantDTO> getAllParticipants() {
        return participantRepository.findAll().stream().map(p -> {
            List<InscriptionInfoDTO> formations = inscriptionRepository.findByParticipantId(p.getId())
                    .stream()
                    .filter(ins -> ins.getStatut() == null || "CONFIRMEE".equals(ins.getStatut()))
                    .map(ins -> InscriptionInfoDTO.builder()
                            .theme(ins.getCycle().getTheme())
                            .dateDebut(ins.getCycle().getDate_deb())
                            .numSalle(ins.getCycle().getNum_salle())
                            .statut(ins.getStatut())
                            .build())
                    .collect(Collectors.toList());
            if (formations.isEmpty() && p.getTheme_part() != null && !p.getTheme_part().isBlank()) {
                formations.add(InscriptionInfoDTO.builder()
                        .theme(p.getTheme_part())
                        .dateDebut(p.getDate_debut())
                        .numSalle(p.getNum_salle())
                        .statut("CONFIRMEE")
                        .build());
            }

            return ParticipantDTO.builder()
                    .id(p.getId())
                    .nom_prenom(p.getNom_prenom())
                    .cin(p.getCin())
                    .mail(p.getMail())
                    .entreprise(p.getEntreprise())
                    .tel_port(p.getTel_port())
                    .tel_fix(p.getTel_fix())
                    .fax(p.getFax())
                    .theme_part(p.getTheme_part())
                    .date_debut(p.getDate_debut())
                    .num_salle(p.getNum_salle())
                    .formations(formations)
                    .build();
        }).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantDTO> getParticipantById(@PathVariable Integer id) {
        return participantRepository.findById(id)
                .map(ParticipantMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/new-participant")
    public ResponseEntity<ParticipantDTO> createParticipant(@RequestBody ParticipantDTO participantDTO) {
        Participant participant = ParticipantMapper.toEntity(participantDTO);
        Participant savedParticipant = participantRepository.save(participant);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ParticipantMapper.toDTO(savedParticipant));
    }

    @PutMapping("/update-participant/{id}")
    public ResponseEntity<ParticipantDTO> updateParticipant(@PathVariable Integer id, @RequestBody ParticipantDTO participantDetailsDTO) {
        return participantRepository.findById(id)
                .map(participant -> {
                    participant.setNom_prenom(participantDetailsDTO.getNom_prenom());
                    participant.setCin(participantDetailsDTO.getCin());
                    participant.setEntreprise(participantDetailsDTO.getEntreprise());
                    participant.setTel_fix(participantDetailsDTO.getTel_fix());
                    participant.setFax(participantDetailsDTO.getFax());
                    participant.setTel_port(participantDetailsDTO.getTel_port());
                    participant.setMail(participantDetailsDTO.getMail());
                    participant.setTheme_part(participantDetailsDTO.getTheme_part());
                    participant.setDate_debut(participantDetailsDTO.getDate_debut());
                    participant.setNum_salle(participantDetailsDTO.getNum_salle());

                    Participant updatedParticipant = participantRepository.save(participant);
                    return ResponseEntity.ok(ParticipantMapper.toDTO(updatedParticipant));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete-participant/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Integer id) {
        return participantRepository.findById(id)
                .map(participant -> {
                    participantRepository.delete(participant);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}