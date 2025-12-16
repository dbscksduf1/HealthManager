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
public class AIHealthAssistantController {

    private final AIService aiService;

    @PostMapping("/assistant")
    public ResponseEntity<?> assistant(@RequestBody Map<String, String> body) {

        String type = body.get("type");
        String exercise = body.getOrDefault("exercise", "");

        if (type == null || type.isBlank()) {
            return ResponseEntity.badRequest().body("type 값이 필요합니다.");
        }

        try {
            String result = aiService.handleRequest(type, exercise);
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            e.printStackTrace();   // 🔥 콘솔에 실제 오류 출력
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }

    }
}
