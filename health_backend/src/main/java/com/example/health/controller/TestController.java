package com.example.health.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



//서버가 정상적으로 동작 중인지 확인하기 위한 테스트용 컨트롤러

@RestController
public class TestController {


    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }
}
