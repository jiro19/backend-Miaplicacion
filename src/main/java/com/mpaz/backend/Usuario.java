package com.mpaz.backend;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true, nullable = false)
    public String username;

    public String password;

    // Roles: ESTUDIANTE, DOCENTE, UTP
    public String role;

    public Usuario() {
    }
}