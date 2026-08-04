package com.enicar.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "participant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "nom_prenom", nullable = false)
    private String nom_prenom;
    @Column(name = "cin", nullable = false)
    private String cin;
    @Column(name = "entreprise", nullable = false)
    private String entreprise;
    @Column(name = "tel_fix", nullable = true)
    private Integer tel_fix;
    @Column(name = "fax", nullable = true)
    private String fax;
    @Column(name = "tel_port", nullable = false)
    private Integer tel_port;
    @Column(name = "mail", nullable = false, unique = true)
    private String mail;
    @Column(name = "password")
    private String password;
    @Column(name = "theme_part", nullable = true)
    private String theme_part;
    @Column(name = "num_salle", nullable = true)
    private Integer num_salle;
    @Column(name = "date_debut", nullable = true)
    private LocalDate date_debut;
}
