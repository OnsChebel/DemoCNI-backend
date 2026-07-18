package com.enicar.demo.repository;

import com.enicar.demo.model.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CycleRepository extends JpaRepository<Cycle, Integer> {
}
