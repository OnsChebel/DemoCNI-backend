package com.enicar.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "formateur")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Formateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "nom_prenom", nullable = false)
    private String nom_prenom;
    @Column(name = "specialite", nullable = false)
    private String specialite;
    @Column(name = "direction", nullable = false)
    private String direction;
    @Column(name = "entreprise", nullable = false)
    private String entreprise;
    @Column(unique = true, nullable = false)
    private String login;
    private String password;
    @Builder.Default
    private Boolean isFirstLogin = true;
}
