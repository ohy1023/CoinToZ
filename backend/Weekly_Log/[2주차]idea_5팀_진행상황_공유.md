## 팀 구성원, 개인 별 역할

---

아이디어톤 팀 구성원을 기재해 주시고, 그 주의 팀원이 어떤 역할을 맡아서 개발을 진행했는지 구체적으로 작성해 주세요. 🙂 

- **강동연 : PM**
- **강수빈 : 인프라**
- **김상호 : 기획**
- **오형상 : CTO**
- **손세열 : 개발1**
- **임학준 : 개발2**

## 팀 내부 회의 진행 회차 및 일자

---

- **1회차(2023.01.27) Discord 음성 채널 회의 진행**

## 현재까지 개발 과정 요약 (최소 500자 이상)

---

현재까지 개발을 진행하면서 ‘기술적으로 새로 알게된 점, 어려웠던 점, 아쉬운 점' 등을 요약하여 작성해 주세요 🙂

팀원 각자 현재 구현하고 있는 것을 적어주세요. :)

- **강동연**
    - React와 Spring Boot 연동하기
    - React와 Spring Boot CI/CD → 실패,, 빌드 스크립트의 문제인지 무엇이 문제인지 모르겠다..
    - 업비트 파라비터와 jwt 있는 경우, `invalid_query_payload` 에러 발생 → 해결 중
- **강수빈**
    - 업비트 API 주문하기 구현
    - 업비트 파라비터와 jwt 있는 경우, `invalid_query_payload` 에러 발생
    - `invalid_query_payload` 에러 발생 원인 : `@FeignClient` 에서 json 형식으로 요청을 보내야 함
    - 업비트 API 주문하기 에러 해결
- **김상호**
    - 회원가입 이메일 인증 기능 구현
    - 이메일 인증 후 회원가입 버튼 활성화 기능 구현 중
- **손세열**
    - 메인페이지 UI 구현
- **오형상**
    - React 회원가입, 로그인 화면
    - OAuth 로그인 성공 시 응답 헤더의 토큰 값을 쿼리 파라미터로 리다이엑션 (리액트에서 파싱 후 쿠키에 저장할 예정)
    - 자체 로그인 성공 시 쿠키에 토큰 저장
    - 쿠키에 있는 토큰 가져와 다른 api 요청 시도 중
- **임학준**
    - 대댓글 기능 구현
    - 댓글, 대댓글 UI 구현중
    - 좋아요, 싫어요 UI 구현중

## 개발 과정에서 나왔던 질문 (최소 200자 이상)

---

개발을 진행하며 나왔던 질문 중 핵심적인 것을 요약하여 작성해 주세요 🙂

질의응답 과정 중 해결되지 않은 질문을 정리하여도 좋습니다.

- **강동연**
    - [[질문]업비트에 Post 요청 보내기](https://www.notion.so/Post-1275be1293f9474bba3842f4534d28fe)
    - [https://velog.io/@armton/Spring-Boot-react-프로젝트-연동하기-2](https://velog.io/@armton/Spring-Boot-react-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%97%B0%EB%8F%99%ED%95%98%EA%B8%B0-2) → Build 파일 작성 후, CI/CD 실패 에러 미해결
- **강수빈**
    - 업비트 API 주문하기 구현 중 `invalid_query_payload` 에러 발생
    - `@FeignClient` 환경에서 JSON 형식으로 요청을 보내는 것에서 문제 발생
    - `@ResponseBody` 를 통해서 Map<,> 형태의 객체의 값을 JSON 형식으로 보낼 수 있음
    - 고유 업비트 token 을 db 에 저장하고 쓴다 가정할 때 보안을 어떻게 해야 할지 고민

## 개발 결과물 공유

---

### **GitLab Repository URL**

[people of development / financial-final-project · GitLab](https://gitlab.com/people-of-development/financial-final-project)

- 팀원들과 함께 찍은 인증샷(온라인 만남시 스크린 캡쳐)도 함께 업로드 해주세요 🙂

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/af9a64f3-b86e-4d91-9418-e37cd21a11ca/Untitled.png)