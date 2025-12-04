package com.example.health.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BmiRequest {
    private double height;
    private double weight;
}
