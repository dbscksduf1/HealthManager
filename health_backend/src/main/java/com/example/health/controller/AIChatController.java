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

/**
 * AIChatController
 * - OpenAI Chat Completion API를 호출하여
 *   운동/식단 관련 AI 답변을 제공하는 컨트롤러
 * - 클라이언트로부터 메시지를 받아 AI 응답을 그대로 반환
 */
@RestController
@RequestMapping("/ai")
public class AIChatController {

    /**
     * application.properties에 설정된 OpenAI API 키
     */
    @Value("${openai.api-key}")
    private String apiKey;


    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    /**
     * AI 채팅 요청 처리
     * @param body 클라이언트에서 전달된 메시지(JSON)
     * @param request HTTP 요청 정보
     * @return AI 응답 결과 또는 오류 메시지
     */
    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String, String> body,
                                  HttpServletRequest request) {

        // 사용자가 입력한 메시지
        String userMessage = body.get("message");

        try {
            // OpenAI API 호출을 위한 RestTemplate 생성
            RestTemplate rest = new RestTemplate();

            // OpenAI 요청 바디 구성
            Map<String, Object> reqBody = Map.of(
                    "model", "gpt-4o-mini",
                    "messages", new Object[]{
                            // 시스템 프롬프트: AI 역할 및 응답 형식 규칙 정의
                            Map.of("role", "system", "content",
                                    "너는 전문 트레이너 + 식단관리 전문가야.\n" +
                                            "💡모든 답변은 아래 규칙을 따른다:\n" +
                                            "1) 문장은 매우 짧게.\n" +
                                            "2) 가능한 bullet point(•) 형태로 정리.\n" +
                                            "3) 문단마다 줄바꿈(빈줄) 넣기.\n" +
                                            "4) 운동루틴은 'Day1 / Day2'로 나누기.\n" +
                                            "5) 식단은 아침/점심/저녁으로 나누고 각 2~3줄만.\n" +
                                            "6) 너무 긴 문장은 절대 금지.\n" +
                                            "7) 말투는 친절하고 단정하게.\n"
                            ),
                            // 사용자 메시지
                            Map.of("role", "user", "content", userMessage)
                    }
            );

            // HTTP 헤더 설정 (Authorization, Content-Type)
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 엔티티 생성
            HttpEntity<Object> entity = new HttpEntity<>(reqBody, headers);

            // OpenAI API 호출
            ResponseEntity<Map> res =
                    rest.exchange(OPENAI_URL, HttpMethod.POST, entity, Map.class);

            // 응답에서 첫 번째 결과 메시지 추출
            Map choice = (Map) ((java.util.List) res.getBody().get("choices")).get(0);
            Map msg = (Map) choice.get("message");

            // AI 응답 내용 반환
            return ResponseEntity.ok(Map.of("result", msg.get("content")));

        } catch (Exception e) {
            // 예외 발생 시 서버 오류 반환
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "AI 처리 중 오류 발생"));
        }
    }
}
