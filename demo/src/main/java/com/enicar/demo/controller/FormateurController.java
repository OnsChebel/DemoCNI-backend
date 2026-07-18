package com.enicar.demo.controller;


import com.enicar.demo.model.Formateur;
import com.enicar.demo.repository.FormateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formateurs")
@CrossOrigin(origins = "http://localhost:4200")
public class FormateurController {
    @Autowired
    private FormateurRepository formateurRepository;

    @GetMapping
    public List<Formateur> getAllFormateurs() {return formateurRepository.findAll();}

    @GetMapping("/{id}")
    public ResponseEntity<Formateur> getFormateurById(@PathVariable Integer id) {
        return formateurRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/new-formateur")
    public ResponseEntity<Formateur> createFormateur(@RequestBody Formateur formateur) {
        Formateur savedFormateur = formateurRepository.save(formateur);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFormateur);
    }

    @PutMapping("/update-formateur/{id}")
    public ResponseEntity<Formateur> updateFormateur(@PathVariable Integer id, @RequestBody Formateur formateurDetails) {
        return formateurRepository.findById(id)
                .map( formateur -> {
                    formateur.setNom_prenom(formateurDetails.getNom_prenom());
                    formateur.setSpecialite(formateurDetails.getSpecialite());
                    formateur.setDirection(formateurDetails.getDirection());
                    formateur.setEntreprise(formateurDetails.getEntreprise());

                    Formateur updatedFormateur = formateurRepository.save(formateur);
                    return ResponseEntity.ok(updatedFormateur);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete-formateur/{id}")
    public ResponseEntity<Void> deleteFormateur(@PathVariable Integer id) {
        return formateurRepository.findById(id)
                .map( formateur -> {
                    formateurRepository.delete(formateur);
                    return ResponseEntity.ok().<Void> build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
