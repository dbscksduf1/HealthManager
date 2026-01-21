package com.example.health.controller;

import com.example.health.dto.HealthStatusResponse;
import com.example.health.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private final AIService aiService;

    // ✅ [추가된 부분] 헬스 체크용 API (인증 없이 사용)
    @GetMapping
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    /**
     * 건강 상태 조회 API
     * @param height 사용자 키(cm)
     * @param weight 사용자 몸무게(kg)
     * @return BMI, 목표(goal), 운동 루틴, AI 코멘트
     */
    @GetMapping("/status")
    public ResponseEntity<?> status(
            @RequestParam(required = false) Double height,
            @RequestParam(required = false) Double weight
    ) {

        if (height == null || height <= 0) {
            throw new IllegalArgumentException("키를 올바르게 입력해주세요.");
        }

        if (weight == null || weight <= 0) {
            throw new IllegalArgumentException("몸무게를 올바르게 입력해주세요.");
        }

        double bmi = weight / Math.pow(height / 100.0, 2);

        String goal;
        if (bmi < 18.5) goal = "벌크업";
        else if (bmi < 23) goal = "린매스업";
        else goal = "다이어트";

        Map<String, Object> routine = generateRoutine(goal);
        String aiComment = aiService.generateComment(bmi, goal);

        HealthStatusResponse response =
                new HealthStatusResponse(bmi, goal, routine, aiComment);

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> generateRoutine(String goal) {

        Map<String, List<String>> bulk = Map.of(
                "day1", List.of(
                        "DAY1 - 등·어깨",
                        "바벨로우 5x5",
                        "풀업 4x6",
                        "덤벨숄더프레스 5x5",
                        "사레레 4x12",
                        "시티드로우 4x8"
                ),
                "day2", List.of(
                        "DAY2 - 가슴·팔",
                        "벤치프레스 5x5",
                        "인클라인덤벨프레스 4x6",
                        "푸쉬다운 4x10",
                        "이두컬 4x10",
                        "딥스 4x8"
                ),
                "day3", List.of(
                        "DAY3 - 하체·복근",
                        "스쿼트 5x5",
                        "루마니안데드리프트 4x6",
                        "레그프레스 4x10",
                        "레그컬 4x12",
                        "행잉레그레이즈 4x12"
                )
        );

        Map<String, List<String>> lean = Map.of(
                "day1", List.of(
                        "DAY1 - 등·어깨",
                        "랫풀다운 4x12",
                        "덤벨로우 4x10",
                        "오버헤드프레스 4x10",
                        "사레레 4x15",
                        "케이블페이스풀 4x15"
                ),
                "day2", List.of(
                        "DAY2 - 가슴·팔",
                        "벤치프레스 4x10",
                        "딥스 3x12",
                        "케이블플라이 4x15",
                        "이두컬 4x12",
                        "푸쉬다운 4x15"
                ),
                "day3", List.of(
                        "DAY3 - 하체·복근",
                        "레그프레스 4x12",
                        "런지 4x10",
                        "스티프데드 4x10",
                        "레그익스텐션 4x15",
                        "크런치 4x20"
                )
        );

        Map<String, List<String>> cut = Map.of(
                "day1", List.of(
                        "DAY1 - 등·어깨",
                        "랫풀다운 4x15",
                        "케이블로우 4x15",
                        "숄더프레스 4x12",
                        "사레레 4x20",
                        "유산소 20분"
                ),
                "day2", List.of(
                        "DAY2 - 가슴·팔",
                        "체스트프레스 4x15",
                        "푸쉬업 4x20",
                        "케이블플라이 4x20",
                        "이두컬 4x15",
                        "유산소 20분"
                ),
                "day3", List.of(
                        "DAY3 - 하체·복근",
                        "레그프레스 4x15",
                        "스텝업 4x12",
                        "레그컬 4x15",
                        "하복근 4x20",
                        "유산소 30분"
                )
        );

        return switch (goal) {
            case "벌크업" -> new HashMap<>(bulk);
            case "린매스업" -> new HashMap<>(lean);
            default -> new HashMap<>(cut);
        };
    }
}
