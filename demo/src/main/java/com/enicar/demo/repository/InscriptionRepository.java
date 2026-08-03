package com.enicar.demo.repository;

import com.enicar.demo.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Integer> {
    List<Inscription> findByParticipantId(Integer participantId);
    Optional<Inscription> findByParticipantIdAndCycleId(Integer participantId, Integer cycleId);
}