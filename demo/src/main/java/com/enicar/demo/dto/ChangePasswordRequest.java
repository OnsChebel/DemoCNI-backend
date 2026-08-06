package com.enicar.demo.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private Integer formateurId;
    private String ancienPassword;
    private String nouveauPassword;
}