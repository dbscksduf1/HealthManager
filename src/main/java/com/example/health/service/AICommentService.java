package com.example.health.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AICommentService {

    @Value("${openai.api-key}")
    private String apiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String generateComment(double bmi, String goal) {

        String prompt = """
                당신은 전문 피트니스 트레이너입니다.
                다음 사용자 정보를 기반으로 3~5줄 정도의 피드백을 작성하세요.
                - BMI: %.2f
                - 목표: %s
                비전문가도 이해하기 쉬운 말로 부드럽게 설명하세요.
                출력 형식:
                문장1
                문장2
                문장3
                """.formatted(bmi, goal);

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

            // 결과 파싱
            Map choice = (Map) ((java.util.List) response.getBody().get("choices")).get(0);
            Map message = (Map) choice.get("message");
            return message.get("content").toString();

        } catch (Exception e) {
            System.out.println("🔥 OpenAI API 호출 오류 발생:");
            e.printStackTrace();
            return "AI 코멘트 생성 실패: " + e.getMessage();
        }
    }
}
