package com.enicar.demo.controller;


import com.enicar.demo.model.Cycle;
import com.enicar.demo.repository.CycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cycles")
@CrossOrigin(origins = "http://localhost:4200")
public class CycleController {
    @Autowired
    private CycleRepository cycleRepository;

    @GetMapping
    public List<Cycle> getAllCycles() {return cycleRepository.findAll();}

    @GetMapping("/{id}")
    public ResponseEntity<Cycle> getCycleById(@PathVariable Integer id) {
        return cycleRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/new-cycle")
    public ResponseEntity<Cycle> createCycle(@RequestBody Cycle cycle) {
        Cycle savedCycle = cycleRepository.save(cycle);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCycle);
    }

    @PutMapping("/update-cycle/{id}")
    public ResponseEntity<Cycle> updateCycle(@PathVariable Integer id, @RequestBody Cycle cycleDetails) {
        return cycleRepository.findById(id)
                .map(cycle -> {
                    cycle.setNum_act(cycleDetails.getNum_act());
                    cycle.setTheme(cycleDetails.getTheme());
                    cycle.setDate_deb(cycleDetails.getDate_deb());
                    cycle.setDate_fin(cycleDetails.getDate_fin());
                    cycle.setFor1(cycleDetails.getFor1());
                    cycle.setFor2(cycleDetails.getFor2());
                    cycle.setFor3(cycleDetails.getFor3());
                    cycle.setNum_salle(cycleDetails.getNum_salle());

                    Cycle updatedCycle = cycleRepository.save(cycle);
                    return ResponseEntity.ok(updatedCycle);
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
