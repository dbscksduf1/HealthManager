package com.example.health.dto;

import java.util.Map;

public class RecommendResponse {

    private double bmi;
    private String goal;

    private Map<String, Object> day1;
    private Map<String, Object> day2;
    private Map<String, Object> day3;

    private Map<String, String> meals;

    public RecommendResponse(double bmi, String goal,
                             Map<String, Object> day1,
                             Map<String, Object> day2,
                             Map<String, Object> day3,
                             Map<String, String> meals) {

        this.bmi = bmi;
        this.goal = goal;
        this.day1 = day1;
        this.day2 = day2;
        this.day3 = day3;
        this.meals = meals;
    }

    public double getBmi() { return bmi; }
    public String getGoal() { return goal; }
    public Map<String, Object> getDay1() { return day1; }
    public Map<String, Object> getDay2() { return day2; }
    public Map<String, Object> getDay3() { return day3; }
    public Map<String, String> getMeals() { return meals; }
}
