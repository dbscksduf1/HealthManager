package com.example.health.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AIHealthAssistantService {

    @Value("${openai.api-key}")
    private String apiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    private String callOpenAI(String prompt) {
        try {
            RestTemplate rest = new RestTemplate();

            Map<String, Object> body = Map.of(
                    "model", "gpt-4o-mini",
                    "messages", new Object[]{
                            // ⭐ 여기가 핵심: system 규칙 추가
                            Map.of("role", "system", "content",
                                    "너는 전문 트레이너 + 전문 영양사 AI이다.\n" +
                                            "모든 답변은 아래 규칙을 반드시 따른다:\n" +
                                            "1) 핵심만 짧게 4~7줄.\n" +
                                            "2) 문장은 매우 짧고 직관적으로.\n" +
                                            "3) bullet point(•) 중심으로.\n" +
                                            "4) 문단 사이 빈 줄로 가독성 높이기.\n" +
                                            "5) 강조가 필요한 부분은 **굵게** 표시.\n" +
                                            "6) 너무 긴 문장은 절대 금지.\n" +
                                            "7) JSON을 출력하라고 하면 그대로 JSON만 출력.\n"
                            ),
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
            return "AI 응답 실패: " + e.getMessage();
        }
    }

    public String handleRequest(String type, String exerciseName) {

        String prompt = "";

        if ("routine".equals(type)) {
            prompt = """
                    오늘의 운동 루틴을 간결하게 bullet point로 구성해줘.
                    3분할 또는 4분할 중 하나를 선택해서 Day1~3 또는 Day1~4 형식으로 나눠줘.
                    각 Day는 운동 3~4개만 짧게 bullet로 작성.
                    """;
        }

        else if ("meal".equals(type)) {
            prompt = """
                    아침, 점심, 저녁 식단을 JSON 형식으로 작성해줘.
                    각 식단은 name, desc, cal, carb, protein, fat, reason 포함.
                    JSON만 출력.
                    
                    """;
        }

        else if ("beginner".equals(type)) {
            prompt = """
                    헬스 완전 초보자를 위한 조언을 bullet 3~5줄로 짧게 작성.
                    """;
        }

        else if ("fatloss".equals(type)) {
            prompt = """
                    체지방 감량 핵심 팁을 bullet 4~6줄로 짧게 정리해줘.
                    """;
        }

        else if ("bulk".equals(type)) {
            prompt = """
                    벌크업 핵심 조언을 bullet 4~6줄로 간단히 작성.
                    """;
        }

        else if ("exercise_detail".equals(type)) {
            prompt = """
                    선택된 운동: %s

                    이 운동의 올바른 자세, 주의사항, 자극 부위를 bullet 4~6개로 짧게 설명해줘.
                    """.formatted(exerciseName);
        }

        return callOpenAI(prompt);
    }
}
