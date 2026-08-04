package com.enicar.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InscriptionInfoDTO {
    private String theme;
    private LocalDate dateDebut;
    private Integer numSalle;
    private String statut;
}