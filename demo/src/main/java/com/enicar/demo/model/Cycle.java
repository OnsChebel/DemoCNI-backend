package com.enicar.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "cycle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "num_act", nullable = false)
    private String num_act;
    @Column(name = "theme", nullable = false)
    private String theme;
    @Column(name = "date_deb", nullable = false)
    private LocalDate date_deb;
    @Column(name = "date_fin", nullable = false)
    private LocalDate date_fin;
    @Column(name = "for1", nullable = false)
    private String for1;
    @Column(name = "for2", nullable = false)
    private String for2;
    @Column(name = "for3", nullable = false)
    private String for3;
    @Column(name = "num_salle", nullable = false)
    private Integer num_salle;
}
