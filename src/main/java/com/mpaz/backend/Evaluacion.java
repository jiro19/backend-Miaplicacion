package com.mpaz.backend;

import jakarta.persistence.*;

@Entity
@Table(name = "evaluaciones")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String studentUsername;
    public String subject;
    public int correctAnswers;
    public int totalQuestions;

    public Evaluacion() {
    }
}