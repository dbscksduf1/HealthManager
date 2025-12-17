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
 * AIHealthAssistantController
 * - 사용자의 요청 에 따라
 *   운동/건강 관련 AI 응답을 제공하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIHealthAssistantController {

    /**
     * AI 요청 처리를 담당하는 서비스
     */
    private final AIService aiService;

    /**
     * AI 헬스 어시스턴트 요청 처리
     * @param body 요청 타입과 운동 정보를 포함한 요청 데이터
     * @return AI가 생성한 응답 결과
     */
    @PostMapping("/assistant")
    public ResponseEntity<?> assistant(@RequestBody Map<String, String> body) {

        // 요청 타입
        String type = body.get("type");

        // 선택적으로 전달되는 운동 이름
        String exercise = body.getOrDefault("exercise", "");

        // 요청 타입이 없는 경우 예외 처리
        if (type == null || type.isBlank()) {
            return ResponseEntity.badRequest().body("type 값이 필요합니다.");
        }

        try {
            // 요청 타입에 따라 AI 서비스 처리
            String result = aiService.handleRequest(type, exercise);

            // AI 응답 결과 반환
            return ResponseEntity.ok(Map.of("result", result));

        } catch (Exception e) {
            // 예외 발생 시 콘솔에 오류 출력 및 서버 오류 반환
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }

    }
}
