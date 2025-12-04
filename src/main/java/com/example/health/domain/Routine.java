package com.example.health.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 사용자의 루틴인지 저장
    private Long userId;

    private String exerciseName;
    private int sets;
    private int reps;
    private double weight;
    private String date;
}
