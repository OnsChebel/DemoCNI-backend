package com.enicar.demo.controller;

import com.enicar.demo.model.Participant;
import com.enicar.demo.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
@CrossOrigin(origins = "http://localhost:4200")
public class ParticipantController {
    @Autowired
    private ParticipantRepository participantRepository;

    @GetMapping
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Integer id) {
        return participantRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/new-participant")
    public ResponseEntity<Participant> createParticipant(@RequestBody Participant participant) {
        Participant savedParticipant = participantRepository.save(participant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedParticipant);
    }

    @PutMapping("/update-participant/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable Integer id, @RequestBody Participant participantDetails) {
        return participantRepository.findById(id)
                .map(participant -> {
                    participant.setNom_prenom(participantDetails.getNom_prenom());
                    participant.setCin(participantDetails.getCin());
                    participant.setEntreprise(participantDetails.getEntreprise());
                    participant.setTel_fix(participantDetails.getTel_fix());
                    participant.setFax(participantDetails.getFax());
                    participant.setTel_port(participantDetails.getTel_port());
                    participant.setMail(participantDetails.getMail());
                    participant.setTheme_part(participantDetails.getTheme_part());
                    participant.setNum_salle(participantDetails.getNum_salle());
                    participant.setDate_debut(participantDetails.getDate_debut());

                    Participant updatedParticipant = participantRepository.save(participant);
                    return ResponseEntity.ok(updatedParticipant);
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