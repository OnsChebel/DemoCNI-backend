package com.enicar.demo.mapper;

import com.enicar.demo.model.Participant;
import com.enicar.demo.dto.ParticipantDTO;

public class ParticipantMapper {
    public static ParticipantDTO toDTO(Participant participant) {
        if (participant == null) return null;
        return ParticipantDTO.builder()
                .id(participant.getId())
                .nom_prenom(participant.getNom_prenom())
                .cin(participant.getCin())
                .entreprise(participant.getEntreprise())
                .tel_fix(participant.getTel_fix())
                .fax(participant.getFax())
                .tel_port(participant.getTel_port())
                .mail(participant.getMail())
                .theme_part(participant.getTheme_part())
                .num_salle(participant.getNum_salle())
                .date_debut(participant.getDate_debut())
                .build();
    }

    public static Participant toEntity(ParticipantDTO dto) {
        if (dto == null) return null;
        return Participant.builder()
                .id(dto.getId())
                .nom_prenom((dto.getNom_prenom()))
                .cin(dto.getCin())
                .entreprise(dto.getEntreprise())
                .tel_fix(dto.getTel_fix())
                .fax(dto.getFax())
                .tel_port(dto.getTel_port())
                .mail(dto.getMail())
                .theme_part(dto.getTheme_part())
                .num_salle(dto.getNum_salle())
                .date_debut(dto.getDate_debut())
                .build();
    }
}
