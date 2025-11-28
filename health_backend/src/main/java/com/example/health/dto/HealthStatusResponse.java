package com.example.health.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthStatusResponse {
    private double bmi;
    private String goal;
    private Map<String, Object> routine;
    private Map<String, Object> meals;
}
