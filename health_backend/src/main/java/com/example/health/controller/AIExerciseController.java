package com.example.health.controller;

import com.example.health.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIExerciseController {

    private final AIService AiService;

    @PostMapping("/exercise-detail")
    public ResponseEntity<?> detail(@RequestBody Map<String, String> body) {
        String exerciseName = body.get("exercise");
        if (exerciseName == null || exerciseName.isBlank()) {
            return ResponseEntity.badRequest().body("exercise 값이 필요합니다.");
        }

        String result = AiService.generateExerciseDetail(exerciseName);
        return ResponseEntity.ok(Map.of("result", result));
    }
}
