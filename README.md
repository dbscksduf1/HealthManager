# 💪 Health Manager (1인 개발)

> BMI 기반 운동 추천 + AI 헬스 챗봇 서비스

---

# 📑 목차

- [프로젝트 소개](#intro)
- [기술 스택](#stack)
- [주요 기능](#feature)
- [시스템 아키텍처](#arch)
- [JWT 인증흐름](#jwt)
- [핵심 구현 및 성능 개선](#core)
- [서비스 화면](#ui)
- [트러블슈팅](#trouble)
- [회고](#review)

---

# 프로젝트 소개 <a name="intro"></a>

Health Manager는 사용자의 **키와 체중 데이터를 기반으로 BMI를 계산하고**,  
목표(벌크업 / 린매스업 / 다이어트)에 맞는 운동 루틴을 추천하는 웹 서비스입니다.  

또한 OpenAI API를 활용한 **AI 헬스 챗봇 기능**을 통해  
사용자에게 맞춤형 운동 및 건강 조언을 제공합니다.  

- JWT 기반 인증 시스템 적용
- OpenAI API 기반 AI 챗봇
- 사용자 맞춤형 운동 추천 시스템

---

# 기술 스택 <a name="stack"></a>

## 🔸 Backend
<p>
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"/>
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"/>
  <img src="https://img.shields.io/badge/JPA-59666C?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/OpenAI%20API-412991?style=for-the-badge&logo=openai&logoColor=white"/>
</p>

## 🔸 Frontend
<p>
  <img src="https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB"/>
</p>

## 🔸 Infra / DB
<p>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Render-000000?style=for-the-badge&logo=render&logoColor=white"/>
  <img src="https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white"/>
</p>

---

# 주요 기능 <a name="feature"></a>

- JWT 기반 회원가입 / 로그인
- BMI 계산 및 목표 자동 분류
- 목표 기반 운동 루틴 추천
- OpenAI 기반 AI 헬스 챗봇
- 사용자 정보 관리

---

# 시스템 아키텍처 <a name="arch"></a>

<p align="center">
  <img src="images/아키텍처.PNG" />
</p>

### 1. React → Spring Boot
로그인 이후 헤더에 JWT를 포함한 API 요청 전송  

### 2. Spring Security (JWT 검증)
인증 필터(JWT Filter)를 통해 사용자 인증 처리  


### 3. 컨트롤러는 HTTP요청처리, 실제 비즈니스 로직은 Service계층에서 처리
BMI 계산 및 목표 분류  

### 4. OpenAI API 호출
챗봇 요청 시 AI 응답 생성 후 Service계층에서 응답데이터 가공 후 Controller로 전달

### 5. Cache 처리
동일 요청 시 캐시된 응답 반환  

### 6. Database 저장
데이터 저장/조회 필요시 Repository 호출 후 사용자 정보 및 결과 저장  

---
# JWT인증 흐름 <a name="jwt"></a>
### 1. 사용자가 로그인을 하면 JWT토큰을 발급받음
### 2. 이후 모든 API요청은 Authorization헤더에 JWT토큰을 포함하여 전송
### 3. Spring Security의 JWT Filter에서 토큰을 추출하고 검증
### 4. 인증객체를 Security Context에 저장
### 5. 이후 요청 처리 시 Security Context에 저장된 인증 정보를 기반으로 사용자 식별
### 6. 인증된 사용자만 Controller 및 Service 로직에 접근 가능하도록 인가 처리
---
# 핵심 구현 및 성능 개선 <a name="core"></a>


### 1. JWT 기반 인증 시스템

- Stateless 구조의 JWT 인증 방식 적용  
- Spring Security 필터 체인을 통해 요청마다 토큰 검증  

**결과**
- 서버 세션 제거로 확장성 확보  
- 인증 처리 일관성 유지  



### 2. BMI 기반 비즈니스 로직 구현

- 사용자 입력(height, weight)을 기반으로 BMI 계산  
- BMI 구간에 따라 벌크업 / 린매스업 / 다이어트 자동 분류  

**결과**
- 사용자 맞춤형 운동 방향 자동 설정  



### 3. OpenAI 기반 챗봇 시스템

- 사용자 질문을 기반으로 AI 응답 생성  
- 운동 및 식단 관련 자연어 처리  

**결과**
- 사용자UX를 고려한 AI 기반 서비스로 확장  



### 4. 캐싱을 통한 OpenAI API 호출 최적화

- 동일 질문에 대해 매번 API 호출 발생 문제 확인  
- 질문을 key로 사용하는 캐싱 전략 적용  

**결과**
- 응답 속도: 약 2000ms → 200ms 개선  
- API 비용 절감  



### 5. K6 부하 테스트를 통한 서버 안정성 검증

- K6를 활용하여 동시 사용자 200명 환경에서 부하 테스트 수행  
- 로그인, BMI 계산, 챗봇 API를 대상으로 테스트 진행  

**결과**
- 평균 응답 시간 안정적으로 유지  
- 에러율 0% 수준 유지  
- 트래픽 증가 상황에서도 서버 안정성 확인  



### 6. JPA 기반 데이터 접근 및 DB 설계

- Spring Data JPA를 활용하여 ORM 기반 데이터 처리 구현  
- Hibernate를 통해 객체와 테이블 간 매핑 자동화  
- MySQL 환경에서 동일한 Entity 구조 유지  
- 보안을 고려하여 DB 비밀번호 및 OpenAI API Key를 코드에 직접 작성하지 않고 환경변수로 관리
  
**결과**
- 반복적인 SQL 작성 감소  
- 유지보수성 및 개발 생산성 향상  
- DB 변경에도 유연한 구조 확보





---

# 서비스 화면 <a name="ui"></a>

## 🔐 로그인 페이지
![로그인화면](images/login.png)

---

## 📝 회원가입 페이지
![회원가입 화면](images/join.png)

---

## 🏠 메인 페이지
![메인 화면](images/main.png)

---

## 📊 BMI 계산 페이지
![bmi계산 화면](images/bmi계산하기.PNG)

---
## 💪 운동 자세 페이지
![운동 자세](images/exercise.PNG)

## AI 챗봇
![AI 챗봇](images/pt챗봇.PNG)

---

# 트러블슈팅 <a name="trouble"></a>

### 1. OpenAI API 중복 호출 문제

- **문제**  
  동일 질문에도 매번 API 호출 발생  

- **원인**  
  응답을 저장하지 않는 구조  

- **해결**  
  캐싱방식을 적용하여 OpenAI API반복호출을 제거(성능 최적화)

- **결과**  
  응답 속도 개선 및 비용 절감  

---
### 2. 예외 처리 분산으로 인한 유지보수 문제

- **문제**  
  각 컨트롤러 및 서비스에서 개별적으로 예외를 처리하여 코드가 분산되고 복잡해짐

- **원인**  
  공통적인 예외 처리 로직이 없어 중복 코드 발생 및 관리 어려움  

- **해결**  
  @ControllerAdvice를 활용한 전역 예외 처리 적용  
  공통 에러 응답 형식을 정의하여 일관된 에러 처리 구조 구성  

- **결과**  
  - 예외 처리 로직 일원화  
  - 코드 중복 제거 및 유지보수성 향상(코드 리팩토링)  
  - 클라이언트에 일관된 에러 응답 제공 



---

# 회고 <a name="review"></a>

단순한 BMI 계산 기능 구현을 넘어, AI 챗봇과 성능 최적화를 적용하며 서비스의 완성도를 높이는 경험을 할 수 있었다.  
특히 OpenAI API 호출 과정에서 캐싱을 적용하며 외부 API 의존도가 높은 시스템에서 성능 최적화와 비용 관리가 중요하다는 것을 체감했다.  

또한 JWT 기반 인증 구조를 직접 구현하며 서버가 상태를 저장하지 않는 Stateless 방식의 장점과, 요청마다 인증을 처리하는 구조에 대한 이해를 높일 수 있었다.  
이번 프로젝트를 통해 기능 구현뿐만 아니라 성능, 인증, 그리고 서비스 안정성까지 함께 고려하는 백엔드 개발의 중요성을 경험했다.

