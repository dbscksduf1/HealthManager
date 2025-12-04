package com.example.health.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AIExerciseService {

    @Value("${openai.api-key}")
    private String apiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String generateExerciseDetail(String exerciseName) {

        String prompt = """
                당신은 전문 퍼스널트레이너입니다.
                사용자가 선택한 운동의 정확한 자세, 주의사항, 자극 부위, 흔한 실수를
                4~6줄 이내로 간단하고 이해 쉬운 말로 설명하세요.
                운동 이름: %s
                출력 형식:
                문장1
                문장2
                문장3
                문장4
                """.formatted(exerciseName);

        try {
            RestTemplate rest = new RestTemplate();

            Map<String, Object> body = Map.of(
                    "model", "gpt-4o-mini",
                    "messages", new Object[]{
                            Map.of("role", "user", "content", prompt)
                    }
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Object> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response =
                    rest.exchange(OPENAI_URL, HttpMethod.POST, request, Map.class);

            Map choice = (Map) ((java.util.List) response.getBody().get("choices")).get(0);
            Map message = (Map) choice.get("message");

            return message.get("content").toString();

        } catch (Exception e) {
            return "AI 운동 설명 생성 실패: " + e.getMessage();
        }
    }
}
