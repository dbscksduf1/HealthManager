package com.example.health.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AIChatController {

    @Value("${openai.api-key}")
    private String apiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String, String> body,
                                  HttpServletRequest request) {

        String userMessage = body.get("message");

        try {
            RestTemplate rest = new RestTemplate();

            Map<String, Object> reqBody = Map.of(
                    "model", "gpt-4o-mini",
                    "messages", new Object[]{
                            Map.of("role", "system", "content",
                                    "너는 전문 트레이너 + 식단관리 전문가야 \n" +
                                            "💡모든 답변은 아래 규칙을 따른다:\n" +
                                            "1) 문장은 매우 짧게.\n" +
                                            "2) 가능한 bullet point(•) 형태로 정리.\n" +
                                            "3) 문단마다 줄바꿈(빈줄) 넣기.\n" +
                                            "4) 운동루틴은 'Day1 / Day2'로 나누기.\n" +
                                            "5) 식단은 아침/점심/저녁으로 나누고 각 2~3줄만.\n" +
                                            "6) 너무 긴 문장은 절대 금지.\n" +
                                            "7) 말투는 친절하고 단정하게.\n"
                            ),
                            Map.of("role", "user", "content", userMessage)
                    }
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> entity = new HttpEntity<>(reqBody, headers);

            ResponseEntity<Map> res =
                    rest.exchange(OPENAI_URL, HttpMethod.POST, entity, Map.class);

            Map choice = (Map) ((java.util.List) res.getBody().get("choices")).get(0);
            Map msg = (Map) choice.get("message");

            return ResponseEntity.ok(Map.of("result", msg.get("content")));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "AI 처리 중 오류 발생"));
        }
    }
}
