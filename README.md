# UniTRIP Backend

대학생 여행 경험을 **기록/공유**하고, 다양한 여행 콘텐츠를 탐색할 수 있는 **대학생 여행 플랫폼 UniTRIP**의 백엔드 서버입니다.  
(여행기/리뷰/추천/동행 등 서비스 기능을 제공하는 REST API 서버)

---
## 🎥 시연영상

https://drive.google.com/file/d/1Wb2P_wfWJ3TrMyaFCL6vWDb0QYv3E6jo/view?usp=sharing

## 👩‍💻 프로젝트 링크

https://solux.tistory.com/186

---

## 🧩 Tech Stack

- **Language**: Java
- **Framework**: Spring Boot
- **Build Tool**: Gradle
- **Security**: Spring Security + JWT
- **Docs**: Swagger (OpenAPI)
- **Infra / Storage**: AWS S3 (이미지 업로드)
- **Server** : EC2
- **etc**: MySQL,RDS

---

## 📌 Key Features (Backend)

- JWT 기반 인증/인가
- 공통 응답 포맷(API Payload) 제공
- 전역 예외 처리 및 에러 코드/상태 관리
- Swagger 기반 API 문서화
- AWS S3 파일 업로드 지원

---

## 🗂️ Project Structure

> 패키지 경로: `src/main/java/com/Solux/UniTrip`
```
src
└─ main
└─ java
└─ com/Solux/UniTrip
├─ common
│ └─ apiPayload
│ ├─ base
│ ├─ exception
│ ├─ handler
│ └─ status
├─ config
│ ├─ ForwardedHeaderConfig.java
│ ├─ S3Config.java
│ ├─ SecurityConfig.java
│ └─ SwaggerConfig.java
├─ jwt
│ ├─ JwtAuthenticationFilter.java
│ └─ JwtTokenProvider.java
├─ controller
├─ dto
├─ entity
├─ repository
├─ service
└─ UniTripApplication.java
```

## 🗂️ Package Overview

### `common`
- 공통 API 응답 포맷과 예외 처리 관리
- 상태 코드, 에러 메시지, 전역 예외 핸들러 포함

### `config`
- Spring Security, Swagger, AWS S3 등 전역 설정
- 보안/문서화/외부 서비스 연동 설정 담당

### `jwt`
- JWT 기반 인증/인가 처리
- 토큰 생성, 검증, 인증 필터 구성

### `controller`
- REST API 엔드포인트 정의
- 요청을 받아 Service 계층으로 전달

### `service`
- 비즈니스 로직 처리
- 도메인 규칙 및 트랜잭션 관리

### `repository`
- JPA 기반 데이터 접근 계층
- 엔티티 조회 및 CRUD 처리

### `entity`
- 데이터베이스 테이블과 매핑되는 도메인 엔티티

### `dto`
- API 요청/응답 DTO 정의
- Controller ↔ Service 간 데이터 전달

---
## 🌍 Service Domain

- **Production**: https://unitrip.duckdns.org

---
## 📄 API Documentation

본 프로젝트는 **Swagger(OpenAPI)** 를 사용하여 API 문서를 제공합니다.  
각 API의 요청/응답 형식과 인증 필요 여부를 확인할 수 있습니다.

- **Service Domain (Production)**  
  https://unitrip.duckdns.org

- **Swagger UI (Local)**  
  http://localhost:8080/swagger-ui/index.html
  
- **OpenAPI JSON**
http://localhost:8080/v3/api-docs

> 모든 API는 공통 응답 포맷을 사용하며,  
> 에러 발생 시에도 상태 코드와 메시지가 일관되게 반환됩니다.

---

## 🔐 Authentication

- 인증 방식: **JWT (JSON Web Token)**
- 로그인 성공 시 **Access Token** 발급
- 인증이 필요한 API 호출 시 Header에 토큰 포함

```http
Authorization: Bearer {ACCESS_TOKEN}
```
- Spring Security + JWT Filter 기반 인증 처리
- 토큰 검증 실패 시 401 Unauthorized 응답 반환


<h2>🙌 Contributors</h2>

<table align="center">
  <tr>
    <td align="center">
      <b>정은서</b><br/>
      <img src="https://github.com/eunseo0903.png" width="200"/><br/>
      <a href="https://github.com/eunseo0903">eunseo0903</a>
    </td>
    <td align="center">
      <b>백다현</b><br/>
      <img src="https://github.com/dahyeon1216.png" width="200"/><br/>
      <a href="https://github.com/dahyeon1216">dahyeon1216</a>
    </td>
    <td align="center">
      <b>정유빈</b><br/>
      <img src="https://github.com/youbing5921.png" width="200"/><br/>
      <a href="https://github.com/youbing5921">youbing5921</a>
    </td>
  </tr>
</table>


---

## 📌 Notes

- 본 프로젝트는 **SOLUX 30기 웹 3팀 활동**의 일환으로 진행되었습니다.
- UniTRIP은 대학생의 여행 경험을 기록하고 공유하기 위한 웹 서비스입니다.
- 본 저장소는 **백엔드 서버 코드**를 포함합니다.
- 보안상 민감한 설정 정보(`application.yml`, 환경 변수 등)는 저장소에 포함하지 않습니다.

