package com.enicar.demo.controller;


import com.enicar.demo.dto.CycleDTO;
import com.enicar.demo.mapper.CycleMapper;
import com.enicar.demo.model.Cycle;
import com.enicar.demo.repository.CycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cycles")
@CrossOrigin(origins = "http://localhost:4200")
public class CycleController {
    @Autowired
    private CycleRepository cycleRepository;

    @GetMapping
    public List<CycleDTO> getAllCycles() {
        return cycleRepository.findAll()
                .stream()
                .map(CycleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CycleDTO> getCycleById(@PathVariable Integer id) {
        return cycleRepository.findById(id)
                .map(CycleMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/new-cycle")
    public ResponseEntity<CycleDTO> createCycle(@RequestBody CycleDTO cycleDTO) {
        Cycle cycle = CycleMapper.toEntity(cycleDTO);
        Cycle savedCycle = cycleRepository.save(cycle);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CycleMapper.toDTO(savedCycle));
    }

    @PutMapping("/update-cycle/{id}")
    public ResponseEntity<CycleDTO> updateCycle(@PathVariable Integer id, @RequestBody CycleDTO cycleDetailsDTO) {
        return cycleRepository.findById(id)
                .map(cycle -> {
                    cycle.setNum_act(cycleDetailsDTO.getNum_act());
                    cycle.setTheme(cycleDetailsDTO.getTheme());
                    cycle.setDate_deb(cycleDetailsDTO.getDate_deb());
                    cycle.setDate_fin(cycleDetailsDTO.getDate_fin());
                    cycle.setFor1(cycleDetailsDTO.getFor1());
                    cycle.setFor2(cycleDetailsDTO.getFor2());
                    cycle.setFor3(cycleDetailsDTO.getFor3());
                    cycle.setNum_salle(cycleDetailsDTO.getNum_salle());

                    Cycle updatedCycle = cycleRepository.save(cycle);
                    return ResponseEntity.ok(CycleMapper.toDTO(updatedCycle));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete-cycle/{id}")
    public ResponseEntity<Void> deleteCycle(@PathVariable Integer id) {
        return cycleRepository.findById(id)
                .map(cycle -> {
                    cycleRepository.delete(cycle);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }


}
