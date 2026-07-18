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
    private Integer cin;
    @Column(name = "entreprise", nullable = false)
    private String entreprise;
    @Column(name = "tel_fix", nullable = false)
    private Integer tel_fix;
    @Column(name = "fax", nullable = false)
    private String fax;
    @Column(name = "tel_port", nullable = false)
    private Integer tel_port;
    @Column(name = "mail", nullable = false)
    private String mail;
    @Column(name = "theme_part", nullable = false)
    private String theme_part;
    @Column(name = "num_salle", nullable = false)
    private int num_salle;
    @Column(name = "date_debut", nullable = false)
    private LocalDate date_debut;
}
