package com.enicar.demo.mapper;

import com.enicar.demo.dto.CycleDTO;
import com.enicar.demo.model.Cycle;

public class CycleMapper {
    public static CycleDTO toDTO(Cycle cycle) {
        if(cycle == null) return null;
        return CycleDTO.builder()
                .id((cycle.getId()))
                .num_act(cycle.getNum_act())
                .theme((cycle.getTheme()))
                .date_deb(cycle.getDate_deb())
                .date_fin((cycle.getDate_fin()))
                .for1(cycle.getFor1())
                .for2(cycle.getFor2())
                .for3(cycle.getFor3())
                .num_salle(cycle.getNum_salle())
                .build();
    }

    public static Cycle toEntity(CycleDTO dto) {
        if(dto == null) return null;
        return Cycle.builder()
                .id(dto.getId())
                .num_act(dto.getNum_act())
                .theme(dto.getTheme())
                .date_deb(dto.getDate_deb())
                .date_fin(dto.getDate_fin())
                .for1(dto.getFor1())
                .for2(dto.getFor2())
                .for3(dto.getFor3())
                .num_salle(dto.getNum_salle())
                .build();
    }
}
