package com.enicar.demo.repository;

import com.enicar.demo.model.Administrateur;
import com.enicar.demo.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, String> {
    Optional<Administrateur> findByLogin(String login);
}
