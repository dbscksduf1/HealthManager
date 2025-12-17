package com.example.health.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


 //사용자 정보를 저장하는 엔티티
 //회원가입, 로그인, 루틴 관리 등
 //사용자와 관련된 모든 기능의 기준이 되는 클래스


@Entity
@Getter
@Table(name = "users")
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String name = "";
    private int age;
    private double height = 0.0;
    private double weight = 0.0;

}
