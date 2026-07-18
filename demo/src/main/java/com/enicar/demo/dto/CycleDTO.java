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
public class CycleDTO {
    private Integer id;
    private String num_act;
    private String theme;
    private LocalDate date_deb;
    private LocalDate date_fin;
    private String for1;
    private String for2;
    private String for3;
    private Integer num_salle;
}
