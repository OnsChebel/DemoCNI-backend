package com.enicar.demo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormateurDTO {
    private Integer id;
    private String nom_prenom;
    private String specialite;
    private String direction;
    private String entreprise;
    private String login;
    private String password;
    @JsonProperty("isFirstLogin")
    private Boolean isFirstLogin;
}
