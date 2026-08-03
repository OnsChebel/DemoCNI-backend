package com.enicar.demo.repository;

import com.enicar.demo.model.Participant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    boolean existsByMail(String mail);
    Optional <Participant> findByMail(String mail);
}
