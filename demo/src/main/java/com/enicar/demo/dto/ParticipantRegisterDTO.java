package com.enicar.demo.dto;

import lombok.Data;

@Data
public class ParticipantRegisterDTO {
    private String nom_prenom;
    private String cin;
    private String mail;
    private String password;
    private Integer tel_port;
    private String entreprise;
}
