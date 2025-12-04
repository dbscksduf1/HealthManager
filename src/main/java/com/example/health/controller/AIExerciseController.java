package com.example.health.controller;

import com.example.health.service.AIExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIExerciseController {

    private final AIExerciseService aiExerciseService;

    @PostMapping("/exercise-detail")
    public ResponseEntity<?> detail(@RequestBody Map<String, String> body) {
        String exerciseName = body.get("exercise");
        if (exerciseName == null || exerciseName.isBlank()) {
            return ResponseEntity.badRequest().body("exercise 값이 필요합니다.");
        }

        String result = aiExerciseService.generateExerciseDetail(exerciseName);
        return ResponseEntity.ok(Map.of("result", result));
    }
}
