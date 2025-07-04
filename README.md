# Q&A 게시판 사이트(BackEnd)

Q&A 게시판 사이트의 백엔드 레파지토리입니다.
회원가입, 로그인, 질문/댓글 CRUD, 신고, 관리자, 기능 등을 구현했습니다.
프론트엔드는 별도 레파지토리로 개발 했습니다.

## 🔨 기술 스택
Language : Java 17
Framework: Spring Boot(Version 3.4.5)
Database : Mysql
Build Tool : Gradle
Security : Spring Security 기반 JWT 및 Refresh Token 방식
ORM : Spring Data JPA
IDE : IntelliJ IDEA
API 테스트 : Postman

## 주요 기능 구현 현황

## ✅ 회원 기능
 - 회원가입(이메일, 비밀번호, 닉네임)
 - 로그인(JWT 발급)
 - 마이페이지 (내 질문 목록 조회) -> 권한 문제 존재

## ✅ 질문 기능
 - 질문 등록
 - 질문 수정
 - 질문 삭제
 - 질문 조회(전체 및 개별 조회)
 - 내 질문만 조회

## ✅ 답변 기능
 - 답변 등록
 - 답변 수정
 - 답변 조회
 - 답변 삭제

## ✅ 신고 기능
 - 신고 추가
 - 신고 삭제 및 조회(유저 조치를 위해 관리자가 확인 가능)

## ✅ 관리자 기능
 - 신고 확인 및 조치
 - 불건전 질문 및 답변 삭제 기능

## Docker
 - Dockerfile 작성 및 테스트 빌드 완료
 - 'docker build' 및 'docker run'을 통해 로컬 컨테이너 실행 확인
 - 향후 'docker-compose' 또는 배포 연계 가능성 고려 중

## 🔨 구현 예정 기능
- 마이페이지 내 활동 내역 상세 조회(작성한 댓글/좋아요 등)
- 댓글 좋아요 기능 추가
- 알림 기능(내 질문에 답변이 달리면 알림)
- 관리자 페이지 UI와 연동 현재 API만 구현
- 권한 처리 개선(마이페이지 등에서 인가 문제 해결 예정)
