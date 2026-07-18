package com.enicar.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantDTO {
    private Integer id;
    private String nom_prenom;
    private Integer cin;
    private String entreprise;
    private Integer tel_fix;
    private String fax;
    private Integer tel_port;
    private String mail;
    private String theme_part;
    private int num_salle;
    private LocalDate date_debut;
}
