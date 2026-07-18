package com.enicar.demo.controller;

import com.enicar.demo.dto.ParticipantDTO;
import com.enicar.demo.mapper.ParticipantMapper;
import com.enicar.demo.model.Participant;
import com.enicar.demo.repository.ParticipantRepository;
import jakarta.servlet.http.Part;
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

    @GetMapping
    public List<ParticipantDTO> getAllParticipants() {
        return participantRepository.findAll()
                .stream()
                .map(ParticipantMapper::toDTO)
                .collect(Collectors.toList());
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
                    participant.setNum_salle(participantDetailsDTO.getNum_salle());
                    participant.setDate_debut(participantDetailsDTO.getDate_debut());

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