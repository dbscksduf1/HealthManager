package com.example.health.controller;

import com.example.health.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * AIExerciseController
 * - 특정 운동 이름을 입력받아
 *   AI를 통해 운동 상세 설명을 생성하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIExerciseController {

    /**
     * AI 관련 비즈니스 로직을 처리하는 서비스
     */
    private final AIService AiService;

    /**
     * 운동 상세 정보 요청 처리
     * @param body 운동 이름을 포함한 요청 데이터
     * @return AI가 생성한 운동 설명 결과
     */
    @PostMapping("/exercise-detail")
    public ResponseEntity<?> detail(@RequestBody Map<String, String> body) {

        // 요청에서 운동 이름 추출
        String exerciseName = body.get("exercise");

        // 운동 이름이 없거나 비어있는 경우 예외 처리
        if (exerciseName == null || exerciseName.isBlank()) {
            return ResponseEntity.badRequest().body("exercise 값이 필요합니다.");
        }

        // AI 서비스를 통해 운동 상세 설명 생성
        String result = AiService.generateExerciseDetail(exerciseName);

        // 생성된 결과 반환
        return ResponseEntity.ok(Map.of("result", result));
    }
}
