package com.example.health.controller;

import com.example.health.dto.HealthStatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/health")
public class HealthController {

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
        Map<String, Object> meals = generateMeals(goal);

        HealthStatusResponse response =
                new HealthStatusResponse(bmi, goal, routine, meals);

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




    private Map<String, Object> generateMeals(String goal) {

        List<Map<String, Object>> meals;

        if (goal.equals("벌크업")) meals = bulkMeals();
        else if (goal.equals("린매스업")) meals = leanMeals();
        else meals = cutMeals();

        Map<String, Object> result = new LinkedHashMap<>();
        String[] keys = {"아침", "점심", "저녁"};

        double tCal = 0, tCarb = 0, tProtein = 0, tFat = 0;

        for (int i = 0; i < 3; i++) {
            Map<String, Object> m = meals.get(i);
            Map<String, Object> total = (Map<String, Object>) m.get("total");

            tCal += (double) total.get("cal");
            tCarb += (double) total.get("carb");
            tProtein += (double) total.get("protein");
            tFat += (double) total.get("fat");

            result.put(keys[i], m);
        }

        result.put("dayTotal", Map.of(
                "cal", tCal,
                "carb", tCarb,
                "protein", tProtein,
                "fat", tFat
        ));

        return result;
    }


    private Map<String, Object> item(String name, double gram,
                                     double cal, double carb, double protein, double fat) {
        return Map.of(
                "name", name,
                "gram", gram,
                "cal", cal,
                "carb", carb,
                "protein", protein,
                "fat", fat
        );
    }


    private Map<String, Object> meal(Map<String, Object>... items) {

        List<Map<String, Object>> list = Arrays.asList(items);

        double cal = 0, carb = 0, protein = 0, fat = 0;

        for (var it : list) {
            cal += (double) it.get("cal");
            carb += (double) it.get("carb");
            protein += (double) it.get("protein");
            fat += (double) it.get("fat");
        }

        return Map.of(
                "items", list,
                "total", Map.of(
                        "cal", cal,
                        "carb", carb,
                        "protein", protein,
                        "fat", fat
                )
        );
    }


    private List<Map<String, Object>> bulkMeals() {

        Map<String, Object> breakfast = meal(
                item("현미밥", 200, 280, 60, 6, 2),
                item("닭가슴살", 150, 165, 0, 35, 2),
                item("삶은 계란", 2, 140, 2, 12, 10),
                item("그릭요거트", 150, 90, 7, 12, 3),
                item("아몬드", 10, 60, 2, 2, 5)
        );

        Map<String, Object> lunch = meal(
                item("고구마", 200, 180, 42, 2, 0),
                item("소고기 스테이크", 200, 450, 0, 40, 30),
                item("샐러드", 200, 50, 9, 3, 0),
                item("훈제계란", 1, 80, 1, 6, 5),
                item("과일(바나나)", 100, 89, 23, 1, 0)
        );

        Map<String, Object> dinner = meal(
                item("파스타", 250, 390, 70, 18, 6),
                item("닭가슴살", 100, 110, 0, 23, 1),
                item("삶은계란", 1, 70, 1, 6, 5),
                item("야채샐러드", 150, 40, 7, 2, 0),
                item("요거트", 100, 59, 3, 10, 0)
        );

        return List.of(breakfast, lunch, dinner);
    }

    private List<Map<String, Object>> leanMeals() {

        Map<String, Object> breakfast = meal(
                item("현미밥", 150, 210, 45, 4, 1),
                item("닭가슴살", 120, 132, 0, 28, 1),
                item("그릭요거트", 100, 59, 3, 10, 0),
                item("아몬드", 10, 60, 2, 2, 5),
                item("삶은계란", 1, 70, 1, 6, 5)
        );

        Map<String, Object> lunch = meal(
                item("고구마", 150, 135, 32, 2, 0),
                item("닭가슴살 샐러드", 200, 220, 12, 30, 5),
                item("계란프라이", 1, 90, 1, 6, 7),
                item("요거트", 100, 59, 3, 10, 0)
        );

        Map<String, Object> dinner = meal(
                item("두부스테이크", 200, 180, 8, 18, 6),
                item("샐러드", 150, 35, 6, 2, 0),
                item("과일", 100, 70, 18, 1, 0),
                item("삶은계란", 1, 70, 1, 6, 5),
                item("닭가슴살", 80, 90, 0, 18, 1)
        );

        return List.of(breakfast, lunch, dinner);
    }

    private List<Map<String, Object>> cutMeals() {

        Map<String, Object> breakfast = meal(
                item("오트밀", 80, 300, 54, 10, 6),
                item("삶은계란", 2, 140, 2, 12, 10),
                item("아몬드", 10, 60, 2, 2, 5),
                item("그릭요거트", 100, 59, 3, 10, 0),
                item("사과", 100, 52, 14, 0, 0)
        );

        Map<String, Object> lunch = meal(
                item("닭가슴살", 150, 165, 0, 35, 2),
                item("샐러드", 200, 50, 9, 3, 0),
                item("고구마", 150, 135, 32, 2, 0),
                item("계란프라이", 1, 90, 1, 6, 7)
        );

        Map<String, Object> dinner = meal(
                item("연어스테이크", 150, 280, 0, 33, 18),
                item("야채샐러드", 150, 40, 7, 2, 0),
                item("단호박", 150, 90, 21, 2, 0),
                item("삶은계란", 1, 70, 1, 6, 5)
        );

        return List.of(breakfast, lunch, dinner);
    }
}
