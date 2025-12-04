package com.example.health.controller;

import com.example.health.dto.RecommendResponse;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/health")
public class HealthRecommendController {

    private Random random = new Random();


    private List<String> pick3(List<String> list) {
        Collections.shuffle(list);
        return list.subList(0, 3);
    }

    @GetMapping("/recommend")
    public RecommendResponse recommend(
            @RequestParam double height,
            @RequestParam double weight
    ) {


        double bmi = weight / Math.pow(height / 100.0, 2);


        String goal;
        if (bmi < 15) goal = "벌크업";
        else if (bmi < 20) goal = "린매스업";
        else goal = "다이어트";



        List<String> back = Arrays.asList(
                "바벨로우", "랫풀다운", "풀업", "시티드로우", "T바로우"
        );
        List<String> shoulder = Arrays.asList(
                "오버헤드프레스", "사레레", "전면레레", "리버스플라이", "덤벨숄더프레스"
        );
        List<String> chest = Arrays.asList(
                "벤치프레스", "인클라인 벤치", "딥스", "체스트플라이", "케이블크로스"
        );
        List<String> arms = Arrays.asList(
                "바벨컬", "해머컬", "케이블푸시다운", "트라이셉스 익스텐션", "덤벨킥백"
        );
        List<String> legs = Arrays.asList(
                "스쿼트", "레그프레스", "루마니안 데드리프트", "레그익스텐션", "레그컬"
        );
        List<String> abs = Arrays.asList(
                "크런치", "레그레이즈", "플랭크", "러시안트위스트", "싯업"
        );


        String rep;
        if (goal.equals("벌크업")) rep = "6~8회 x 4";
        else if (goal.equals("린매스업")) rep = "10~12회 x 4";
        else rep = "15~20회 x 4";


        Map<String, Object> day1 = Map.of(
                "title", "등 + 어깨",
                "exercises", Map.of(
                        "등", pick3(back).stream().map(s -> s + " " + rep).toList(),
                        "어깨", pick3(shoulder).stream().map(s -> s + " " + rep).toList()
                )
        );


        Map<String, Object> day2 = Map.of(
                "title", "가슴 + 팔",
                "exercises", Map.of(
                        "가슴", pick3(chest).stream().map(s -> s + " " + rep).toList(),
                        "팔", pick3(arms).stream().map(s -> s + " " + rep).toList()
                )
        );


        Map<String, Object> day3 = Map.of(
                "title", "하체 + 복근",
                "exercises", Map.of(
                        "하체", pick3(legs).stream().map(s -> s + " " + rep).toList(),
                        "복근", pick3(abs).stream().map(s -> s + " " + rep).toList()
                )
        );


        List<String> breakfast = Arrays.asList(
                "오트밀 + 프로틴", "계란 3 + 바나나", "그릭요거트 + 견과류"
        );
        List<String> lunch = Arrays.asList(
                "현미밥 + 닭가슴살 + 샐러드", "연어덮밥", "소고기 150g + 밥"
        );
        List<String> dinner = Arrays.asList(
                "고구마 + 닭가슴살", "연어구이 + 샐러드", "두부 + 샐러드"
        );

        Map<String, String> meals = Map.of(
                "breakfast", breakfast.get(random.nextInt(breakfast.size())),
                "lunch", lunch.get(random.nextInt(lunch.size())),
                "dinner", dinner.get(random.nextInt(dinner.size()))
        );

        return new RecommendResponse(bmi, goal, day1, day2, day3, meals);
    }
}
