package com.enicar.demo.controller;

import com.enicar.demo.dto.InscriptionDTO;
import com.enicar.demo.model.Cycle;
import com.enicar.demo.model.Inscription;
import com.enicar.demo.model.Participant;
import com.enicar.demo.repository.CycleRepository;
import com.enicar.demo.repository.InscriptionRepository;
import com.enicar.demo.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/inscriptions")
@CrossOrigin(origins = "http://localhost:4200")
public class InscriptionController {

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @GetMapping("/participant/{participantId}")
    public List<Inscription> getInscriptionsByParticipant(@PathVariable Integer participantId) {
        return inscriptionRepository.findByParticipantId(participantId);
    }

    @PostMapping("/inscrire")
    public ResponseEntity<?> inscrire(@RequestBody InscriptionDTO dto) {
        Participant participant = participantRepository.findById(dto.getParticipantId())
                .orElseThrow(() -> new RuntimeException("المشارك غير موجود"));

        Cycle cycle = cycleRepository.findById(dto.getCycleId())
                .orElseThrow(() -> new RuntimeException("الدورة غير موجودة"));

        if (dto.getTel_fix() != null) participant.setTel_fix(dto.getTel_fix());
        if (dto.getFax() != null) participant.setFax(dto.getFax());

        participant.setNum_salle(cycle.getNum_salle());
        participant.setDate_debut(cycle.getDate_deb());
        participant.setTheme_part(cycle.getTheme());

        participantRepository.save(participant);

        Optional<Inscription> existing = inscriptionRepository.findByParticipantIdAndCycleId(dto.getParticipantId(), dto.getCycleId());

        Inscription inscription;
        if (existing.isPresent()) {
            inscription = existing.get();
            inscription.setStatut("CONFIRMEE");
            inscription.setDateInscription(LocalDate.now());
        } else {
            inscription = Inscription.builder()
                    .participant(participant)
                    .cycle(cycle)
                    .dateInscription(LocalDate.now())
                    .statut("CONFIRMEE")
                    .build();
        }

        inscriptionRepository.save(inscription);
        return ResponseEntity.ok(Map.of("message", "تم التسجيل بنجاح وتحديث البيانات !"));
    }

    @PutMapping("/annuler")
    public ResponseEntity<?> annulerInscription(@RequestParam Integer participantId, @RequestParam Integer cycleId) {
        Optional<Inscription> existing = inscriptionRepository.findByParticipantIdAndCycleId(participantId, cycleId);

        if (existing.isPresent()) {
            Inscription inscription = existing.get();
            inscription.setStatut("ANNULEE");
            inscriptionRepository.save(inscription);
            return ResponseEntity.ok(Map.of("message", "تم إلغاء التسجيل بنجاح"));
        }

        return ResponseEntity.badRequest().body(Map.of("message", "التسجيل غير موجود"));
    }
}