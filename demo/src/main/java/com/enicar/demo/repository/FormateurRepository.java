package com.enicar.demo.repository;

import com.enicar.demo.model.Formateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormateurRepository extends JpaRepository<Formateur, Integer> {
    Optional<Formateur> findByLogin(String login);
}
