package com.enicar.demo.repository;

import com.enicar.demo.model.Inscription;
import com.enicar.demo.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Integer> {
    List<Inscription> findByParticipantId(Integer participantId);
    Optional<Inscription> findByParticipantIdAndCycleId(Integer participantId, Integer cycleId);
    @Query("SELECT i.participant FROM Inscription i WHERE i.cycle.id = :cycleId AND i.statut = 'CONFIRMEE'")
    List<Participant> findParticipantsByCycleId(@Param("cycleId") Integer cycleId);
}