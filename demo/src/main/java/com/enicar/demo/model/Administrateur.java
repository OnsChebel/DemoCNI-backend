package com.enicar.demo.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table (name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Administrateur {
    @Id
    @Column(name = "login", nullable = false)
    private String login;
    @Column(name = "pass", nullable = false)
    private String password;
}
