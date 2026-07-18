package com.enicar.demo.controller;


import com.enicar.demo.dto.FormateurDTO;
import com.enicar.demo.mapper.FormateurMapper;
import com.enicar.demo.model.Formateur;
import com.enicar.demo.repository.FormateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/formateurs")
@CrossOrigin(origins = "http://localhost:4200")
public class FormateurController {
    @Autowired
    private FormateurRepository formateurRepository;

    @GetMapping
    public List<FormateurDTO> getAllFormateurs() {
        return formateurRepository.findAll()
                .stream()
                .map(FormateurMapper::toDTO)
                .collect(Collectors.toList());}

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
        Formateur savedFormateur = formateurRepository.save(formateur);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FormateurMapper.toDTO(savedFormateur));
    }

    @PutMapping("/update-formateur/{id}")
    public ResponseEntity<FormateurDTO> updateFormateur(@PathVariable Integer id, @RequestBody FormateurDTO formateurDetailsDTO) {
        return formateurRepository.findById(id)
                .map( formateur -> {
                    formateur.setNom_prenom(formateurDetailsDTO.getNom_prenom());
                    formateur.setSpecialite(formateurDetailsDTO.getSpecialite());
                    formateur.setDirection(formateurDetailsDTO.getDirection());
                    formateur.setEntreprise(formateurDetailsDTO.getEntreprise());

                    Formateur updatedFormateur = formateurRepository.save(formateur);
                    return ResponseEntity.ok(FormateurMapper.toDTO(updatedFormateur));
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
