package com.enicar.demo.repository;

import com.enicar.demo.model.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CycleRepository extends JpaRepository<Cycle, Integer> {
    @Query("SELECT c FROM Cycle c WHERE c.for1 = :name OR c.for2 = :name OR c.for3 = :name")
    List<Cycle> findByFormateurName(@Param("name") String name);
}
