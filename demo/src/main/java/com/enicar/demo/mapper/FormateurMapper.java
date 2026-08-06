package com.enicar.demo.mapper;

import com.enicar.demo.dto.FormateurDTO;
import com.enicar.demo.model.Formateur;

public class FormateurMapper {
    public static FormateurDTO toDTO(Formateur formateur) {
        if(formateur == null) return null;
        return FormateurDTO.builder()
                .id(formateur.getId())
                .nom_prenom(formateur.getNom_prenom())
                .specialite(formateur.getSpecialite())
                .direction(formateur.getDirection())
                .entreprise(formateur.getEntreprise())
                .login(formateur.getLogin())
                .password(formateur.getPassword())
                .isFirstLogin(formateur.getIsFirstLogin())
                .build();
    }

    public static Formateur toEntity(FormateurDTO dto) {
        if(dto == null) return null;
        return Formateur.builder()
                .id(dto.getId())
                .nom_prenom(dto.getNom_prenom())
                .specialite(dto.getSpecialite())
                .direction(dto.getDirection())
                .entreprise(dto.getEntreprise())
                .login(dto.getLogin())
                .password(dto.getPassword())
                .isFirstLogin(dto.isFirstLogin())
                .build();
    }
}
