package com.mpaz.backend;

import jakarta.persistence.*;

@Entity
@Table(name = "materiales")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String subject;      // Lenguaje, Matemática, etc.
    public String unitName;     // Unidad 1, Unidad 2, Prueba Final
    public String oaCode;       // OA 01, OA 02
    public String description;  // Título de la guía

    @Column(length = 3000)
    public String contentBody;  // Texto completo para que el alumno lo descargue

    public String status;       // EN_REVISION, PUBLICADO, RECHAZADO
    public String authorRole;   // DOCENTE o UTP
    public String dateCreated;  // Fecha

    public Material() {
    }
}