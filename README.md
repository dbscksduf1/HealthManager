
#  Health Manager
React + Spring Boot 기반 **BMI 계산 / 운동 루틴 /  헬스 관리 서비스

---

## 📌 프로젝트 소개
Health Manager는 사용자가 **키와 체중을 입력하면 BMI를 계산**하고  
그에 맞춰 **운동 루틴을 자동 추천**해주는 웹 서비스입니다.

- JWT 기반 로그인 인증
- OpenAI API호출을 이용한 헬스챗봇 개발
- MySQL 기반 사용자 관리
- 백엔드+프론트엔드 구조의 풀스택 시스템 


---

## 주요 기능 요약

### 인증(Authentication)
- 회원가입
- 로그인(JWT 발급)
- 인증 사용자만 메인 페이지 접근 가능

### 챗봇
- OpenAI API호출을 이용한 조언과 챗봇시스템

###  BMI 계산
- `height`, `weight` 입력 후 자동 BMI 계산
- BMI 구간에 따라 “벌크업 / 린매스업 / 다이어트” 목표 자동 분류

### 운동 루틴 추천
- 목표에 따라 운동 강도 및 부위 자동 추천


---


## 기술 스택

### 🔸 Language
<p>
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white"/>
</p>

### 🔸 Backend
<p>
  <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"/>
  <img src="https://img.shields.io/badge/REST%20API-000000?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"/>
  <img src="https://img.shields.io/badge/JPA-59666C?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/OpenAI%20API-412991?style=for-the-badge&logo=openai&logoColor=white"/>
</p>

### 🔸 Infra / DB
<p>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Render-000000?style=for-the-badge&logo=render&logoColor=white"/>
</p>

### 🔸 ETC
<p>
  <img src="https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB"/>
</p>


---


###  아키텍처

![아키텍처](images/아키텍처.PNG)


---

## 🌐 동작 흐름

1. 사용자가 React UI에서 로그인/회원가입
2. 백엔드(Spring Boot)가 JWT 발급
3. 프론트엔드는 JWT를 포함하여 API 요청
4. Spring Security에서 JWT 검증
5. 백엔드는 BMI 계산 및 OpenAI API호출 후 사용자조언
6. 챗봇 요청 시 캐시 확인 후 OpenAI API 호출
7. 결과를 프론트엔드로 반환


---


### 🔐 로그인 페이지
![로그인화면](images/login.png)

---
### 📝 회원가입 페이지
![회원가입 화면](images/join.png)

---

### 🏠 메인 페이지
![메인 화면](images/main.png)

---

### 📊 BMI 계산 페이지
![bmi계산]

---

### 🍽️ 맞춤 식단 페이지
![맞춤 식단 화면](images/meal.png)










---




---

## 📦 배포
- Frontend: Vercel 배포 완료
- Backend: Render(Spring Boot 빌드 방식) 배포 완료
- Database: Render MySQL 사용
---

## 👨‍💻 개발자
**윤찬열(Backend + Frontend Full)**    
- Spring Boot API 개발  
- JWT 인증 및 DB 설계  
- 아키텍처 설계 / 테스트 / 디버깅  

